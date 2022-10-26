@file:OptIn(ExperimentalMaterialApi::class)

package com.compose.components

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.graphics.applyCanvas
import com.google.modernstorage.photopicker.PhotoPicker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

/**
 * Enum class that specifies the types of Pages to switch between based on the operation being performed.
 * @property Properties refers to the CropPropertySelectionMenu page => allows users to choose between different crop types like geometry, angle and shapes etc.
 * @property Crop refers to the CropMask page => outputs the cropped image
 */
internal enum class SelectionPage {
    Properties, Crop
}

/**
 * Enum class that specifies the different crop shapes supported by the ImageCropper() component.
 * @property Circle Circle shape geometry - can be customized with different center offset, size etc (Refer drawCircle() in Canvas() composable)
 * @property Rectangle Rectangle shape geometry - can be customized with different size, topLeft offsets (Refer drawRect() in Canvas() composable)
 * @property Hexagon Hexagon shape geometry - Drawn by a custom outline polygon equation polygonOutline()
 * @property QuadraticBezier Bezier curve shape geometry - Drawn by a custom bezier curve equation bezierCurveOutline()
 */
internal enum class CropType{
    Circle, Rectangle, Hexagon, QuadraticBezier
}

/**
 * Data class to store the dimensions for the Crop boundary's bounding box.
 * @param minX Minimum x position held by the CropShape drawn in the Canvas()
 * @param minY Minimum y position held by the CropShape drawn in the Canvas()
 * @param maxX Maximum x position held by the CropShape drawn in the Canvas()
 * @param maxY Minimum x position held by the CropShape drawn in the Canvas()
 */
internal data class BoundingBoxDims(
    private val minX: Float = 0f,
    private val minY: Float = 0f,
    private val maxX: Float = 0f,
    private val maxY: Float = 0f
){
    var leftX = minX
    var leftY = minY
    var rightX = maxX
    var rightY = maxY
}

/**
 * Base composable function that wraps over and displays the entire ImageCropper() component with a BottomSheetScaffold.
 */
@Composable
@Stable
fun ImageCropDemo() {
    val imageBitmapSource = ImageBitmap.imageResource(
        LocalContext.current.resources,
        R.drawable.image
    )

    var imageBitmap by remember { mutableStateOf(imageBitmapSource) }
    var cropImageBitmap by remember { mutableStateOf(imageBitmapSource) }
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            BottomSheetValue.Collapsed
        )
    )
    val scope = rememberCoroutineScope()
    var selectionPage by remember { mutableStateOf(SelectionPage.Properties) }
    var cropType by remember { mutableStateOf(CropType.Circle) }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        content = {
            ImageCropper(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                cropType = cropType,
                imageBitmap = imageBitmap,
                onCropImageBitmapChanged = {
                    cropImageBitmap = it
                },
                onImageBitmapChanged  = {
                    imageBitmap = it
                },
                onSelectionPageChanged = {
                    selectionPage = it
                },
                scope = scope,
            )
        },
        sheetContent =  {
            when (selectionPage) {
                SelectionPage.Properties -> {
                    CropPropertySelectionMenu(
                        onCropPropertiesChanged = {
                            cropType = it
                            scope.launch {
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    )
                }
                SelectionPage.Crop -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(600.dp),
                    ) {
                        CropMask(
                            cropType = cropType,
                            imageBitmap = cropImageBitmap,
                        )
                    }
                }
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface
    )
}

/**
 * Composable function that displays UI for each functionality, namely Cropping and CropMaskOverlay.
 * Responsible for drawing the CropMask() and CropMaskOverlay() UI composables on the Canvas().
 * Provides a customizable BottomAppBar() with functionality to switch between the above different pages and select images from media via ImageSelectionButton() composable
 * @param bottomSheetScaffoldState Variable that holds the current bottomSheetState for collapse/ expand operations
 * @param cropType Variable that holds the current crop type being applied on the selected image
 * @param imageBitmap Variable that holds the current ImageBitmap that needs to be cropped
 * @param onCropImageBitmapChanged Lambda that changes the current cropped imageBitmap to be used for displaying output in the CropMask() composable
 * @param onImageBitmapChanged Lambda that changes the current imageBitmap when a new image is picked from ImageSelectionButton()
 * @param onSelectionPageChanged Lambda that changes the current selection Page to reflect the UI accordingly
 * @param scope Variable that holds the current coroutine scope to launch state transitions
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Stable
internal fun ImageCropper(
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    cropType: CropType,
    imageBitmap: ImageBitmap,
    onCropImageBitmapChanged: (ImageBitmap) -> Unit,
    onImageBitmapChanged: (ImageBitmap) -> Unit,
    onSelectionPageChanged: ( SelectionPage ) -> Unit,
    scope: CoroutineScope,
){

    var scale by remember { mutableStateOf(1f) }
    var rotationState by remember { mutableStateOf(0f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }

    val view = LocalView.current
    var capturingViewBounds by remember { mutableStateOf<Rect?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        Box (
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .onGloballyPositioned {
                    capturingViewBounds = it.boundsInRoot()
                }
        ){
            CropMaskOverlay(
                cropType = cropType,
                imageViewBounds = capturingViewBounds
            )

            Box(
                modifier = Modifier
                    .zIndex(-1f)
                    .clip(RectangleShape)
                    .fillMaxSize()
                    .background(Color.Gray)
                    .pointerInput(Unit) {
                        detectTransformGestures { _, pan, zoom, rotation ->
                            scale *= zoom
                            rotationState += rotation

                            val maxX = (size.width * (scale)) / 2
                            val minX = -maxX
                            offsetX = maxOf(minX, minOf(maxX, offsetX + pan.x))
                            val maxY = (size.height * (scale)) / 2
                            val minY = -maxY
                            offsetY = maxOf(minY, minOf(maxY, offsetY + pan.y))
                        }
                    }
            ) {
                Image(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .graphicsLayer(
                            scaleX = scale.coerceIn(1f, 5f),
                            scaleY = scale.coerceIn(1f, 5f),
                            rotationZ = rotationState,
                            translationX = offsetX,
                            translationY = offsetY
                        ),
                    contentDescription = null,
                    bitmap = imageBitmap
                )
            }
        }

        BottomAppBar(
            actions = {
                IconButton(
                    onClick = {
                        onSelectionPageChanged(SelectionPage.Properties)
                        scope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else{
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                    )
                }

                IconButton(
                    onClick = {
                        onSelectionPageChanged(SelectionPage.Crop)
                        val bounds = capturingViewBounds ?: return@IconButton
                        val cropImageBitmap = Bitmap.createBitmap(
                            bounds.width.toInt(), bounds.height.toInt(),
                            Bitmap.Config.ARGB_8888
                        ).applyCanvas {
                            translate(-bounds.left, -bounds.top)
                            view.draw(this)
                        }.asImageBitmap()

                        onCropImageBitmapChanged(cropImageBitmap)

                        scope.launch {
                            if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            } else{
                                bottomSheetScaffoldState.bottomSheetState.collapse()
                            }
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.Done, contentDescription = "Crop Image"
                    )
                }
            },
            floatingActionButton = {
                ImageSelectionButton(
                    elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 0.dp),
                    onImageSelected = {
                        onImageBitmapChanged(it)
                    }
                )
            }
        )
    }
}

/**
 * Composable that draws the Mask overlay in the Canvas() on top of the imageBitmap to be cropped.
 * @param cropType Variable that holds the current crop type being applied on the selected image
 * @param imageViewBounds Variable that holds the current image's Rect() viewBounds, after all image transformations are applied.
 * This is essentially used to draw a new imageBitmap on a separate Canvas() that is going to be used for the output.
 */
@Composable
@Stable
internal fun CropMaskOverlay(
    cropType: CropType,
    imageViewBounds: Rect?
) {
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = .99f
            }
    ) {
        drawRect(
            color = Color.Black.copy(alpha = 0.7f)
        )
        when (cropType) {
            CropType.Rectangle -> {
                if (imageViewBounds != null) {
                    drawRect(
                        color = Color.Transparent,
                        blendMode = BlendMode.Clear,
                        size = Size(
                            (imageViewBounds.width / 1.5).toFloat(),
                            (imageViewBounds.height / 4)
                        ),
                        topLeft = Offset(
                            x = (imageViewBounds.width / 2) - (imageViewBounds.width / 2.75).toFloat(),
                            y = (imageViewBounds.height / 2) - (imageViewBounds.height / 4)
                        )
                    )
                }
            }
            CropType.Circle -> {
                if (imageViewBounds != null) {
                    drawCircle(
                        color = Color.Transparent,
                        blendMode = BlendMode.Clear,
                    )
                }
            }
            CropType.Hexagon -> {
                if (imageViewBounds != null) {
                    val boundingRectangleDims = calculateBoundingBoxDimensions(
                        offset = Offset(
                            x = ((imageViewBounds.width / 2)),
                            y = ((imageViewBounds.height / 2))
                        ),
                        rotation = 0f,
                        sides = 6,
                        size = Size(
                            (imageViewBounds.width / 1.5).toFloat(),
                            ((imageViewBounds.height / 4))
                        )
                    )

                    Log.d("Bounding", "${boundingRectangleDims.leftX}, ${boundingRectangleDims.leftY}, ${boundingRectangleDims.rightX}, ${boundingRectangleDims.rightY}")
//                    drawRect(
//                        blendMode = BlendMode.SrcIn,
//                        color = Color.White,
//                        topLeft = Offset(boundingRectangleDims.leftX, boundingRectangleDims.leftY),
//                        size = Size(
//                            boundingRectangleDims.rightX - boundingRectangleDims.leftX,
//                            boundingRectangleDims.rightY - 100
//                        )
//                    )

                    drawOutline(
                        blendMode = BlendMode.Clear,
                        color = Color.Transparent,
                        outline = polygonOutline(
                            offset = Offset(
                                x = (imageViewBounds.width / 2),
                                y = (imageViewBounds.height / 2)
                            ),
                            rotation = 0f,
                            size = Size(
                                (imageViewBounds.width / 1.5).toFloat(),
                                (imageViewBounds.height / 4)
                            ),
                            sides = 6
                        )
                    )
                }
            }
            CropType.QuadraticBezier -> {
                if (imageViewBounds != null) {
                    drawOutline(
                        blendMode = BlendMode.Clear,
                        color = Color.Transparent,
                        outline = bezierCurveOutline(
                            offset = Offset(
                                x = ((imageViewBounds.width / 2)),
                                y = ((imageViewBounds.height / 2))
                            )
                        )
                    )
                }
            }
        }
    }
}

/**
 * Composable that crops the current crop mask overlay in the Canvas() on top of the imageBitmap to output a Cropped image.
 * @param cropType Variable that holds the current crop type being applied on the selected image
 * @param imageBitmap Variable that holds the new imageBitmap after applying all the image transitions, this imageBitmap is created only when the crop button is clicked.
 */
@Composable
@Stable
internal fun CropMask(
    cropType: CropType,
    imageBitmap: ImageBitmap,
){
    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                alpha = .99f
            },
        onDraw = {
            when (cropType){
                CropType.Rectangle -> {
                    drawRect(
                        color = Color.Black,
                        size = Size(
                            (imageBitmap.width / 1.5).toFloat(),
                            (imageBitmap.height / 4).toFloat()
                        ),
                        topLeft = Offset(
                            x = (imageBitmap.width / 2).toFloat() - (imageBitmap.width / 2.75).toFloat(),
                            y = (imageBitmap.height / 2).toFloat() - (imageBitmap.height / 4).toFloat()
                        )
                    )
                }
                CropType.Circle -> {
                    drawCircle(
                        color = Color.Black,
                    )
                }
                CropType.Hexagon -> {
                    drawOutline(
                        color = Color.Black,
                        outline = polygonOutline(
                            offset = Offset(
                                x = ((imageBitmap.width / 2).toFloat()),
                                y = ((imageBitmap.height / 2).toFloat())
                            ),
                            rotation = 0f,
                            sides = 6,
                            size = Size(
                                (imageBitmap.width / 1.5).toFloat(),
                                ((imageBitmap.height / 4).toFloat())
                            )
                        )
                    )
                }
                CropType.QuadraticBezier -> {
                    drawOutline(
                        color = Color.Black,
                        outline = bezierCurveOutline(
                            offset = Offset(
                                x = ((imageBitmap.width / 2).toFloat()),
                                y = ((imageBitmap.height / 2).toFloat())
                            )
                        )
                    )
                }
            }

            drawImage(
                image = imageBitmap,
                blendMode = BlendMode.SrcIn
            )
        }
    )
}

/**
 * Page inside the BottomSheetScaffold that allows users to select between multiple different crop type options
 * @param onCropPropertiesChanged Lambda that changes the current crop properties to be used for displaying output in the CropMask() composable
 */
@Composable
@Stable
internal fun CropPropertySelectionMenu(
    onCropPropertiesChanged: ( CropType ) -> Unit
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = {
                onCropPropertiesChanged(CropType.Circle)
            }
        ) {
            Text(
                text = "Circle",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onCropPropertiesChanged(CropType.Rectangle)
            }
        )
        {
            Text(
                text = "Rectangle",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onCropPropertiesChanged(CropType.Hexagon)
            }
        ) {
            Text(
                text = "Hexagon",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onCropPropertiesChanged(CropType.QuadraticBezier)
            }
        ) {
            Text(
                text = "Bezier Curve",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

/**
 * Page that opens Android's in-built PhotoPicker() to select different input images to crop.
 * @param elevation Elevation for the button
 * @param onImageSelected Lambda that provides the ImageCropper() with the current selected image from the PhotoPicker()
 */
@SuppressLint("UnsafeOptInUsageError")
@Composable
@Stable
internal fun ImageSelectionButton(
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    onImageSelected: (ImageBitmap) -> Unit
) {
    val context = LocalContext.current

    val photoPicker = rememberLauncherForActivityResult(PhotoPicker()) { uris ->
        val uri = uris.firstOrNull() ?: return@rememberLauncherForActivityResult

        val bitmap: Bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.decodeBitmap(
                ImageDecoder.createSource(context.contentResolver, uri)
            ) { decoder, _, _ ->
                decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
                decoder.isMutableRequired = true
            }
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        }

        onImageSelected(bitmap.asImageBitmap())
    }

    FloatingActionButton(
        elevation = elevation,
        onClick = {
            photoPicker.launch(PhotoPicker.Args(PhotoPicker.Type.IMAGES_ONLY, 1))
        },
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = null
        )
    }
}

/**
 * Draws a polygon outline/ shape based on the input parameters.
 * @param offset Variable that holds the Offset(x, y) to move/ draw the polygon from the current provided offset. By default shapes are drawn on the Canvas() starting (0, 0) [Top-left]
 * @param rotation Variable that holds the Rotation value for the polygon's vertices
 * @param size Variable that holds the Size(x, y) of the polygon's edges
 * @param sides Variable that holds the number of sides for the polygon to be drawn - Can be customized to draw Pentagon(), Hexagon() etc.
 */
internal fun polygonOutline(
    offset: Offset = Offset(0f, 0f),
    rotation: Float = 0f,
    size: Size = Size(0f, 0f),
    sides: Int = 4
) : Outline {
    return Outline.Generic(
        Path().apply {
            val radius = if (size.width > size.height) size.width / 2f else size.height / 2f
            val angle = 2.0 * Math.PI / sides
            val cx = offset.x
            val cy = offset.y
            val r = rotation * (Math.PI / 180)
            moveTo(
                cx + (radius * cos(0.0 + r).toFloat()),
                cy + (radius * sin(0.0 + r).toFloat())
            )
            for (i in 1 until sides) {
                lineTo(
                    cx + (radius * cos(angle * i + r).toFloat()),
                    cy + (radius * sin(angle * i + r).toFloat())
                )
            }
            close()
        }
    )
}

/**
 * Draws a quadratic bezier curve based on the input parameters.
 * @param offset Variable that holds the Offset(x, y) to move/ draw the curve from the current provided offset. By default shapes are drawn on the Canvas() starting (0, 0) [Top-left]
 */
internal fun bezierCurveOutline(
    offset: Offset
): Outline {
   return Outline.Generic(
       Path().apply {
           moveTo(offset.x, offset.y)
           quadraticBezierTo(
               offset.x + 50.dp.value,
               offset.y + 200.dp.value,
               offset.x + 300.dp.value,
               offset.y + 300.dp.value
           )
           lineTo(offset.x + 270.dp.value, offset.y + 100.dp.value)
           quadraticBezierTo(
               offset.x + 60.dp.value,
               offset.y + 80.dp.value,
               offset.x + 0f,
               offset.y + 0f)
           close()
        }
   )
}

//TODO: Complete the bounding box implementation and cleanup the code to reuse existing code from polygonOutline function
/**
 * Draws a bounding box (Rectangle) around the current drawn geometry. This enables the user to snap the edges of the image to the edges of the rectangle, thereby maintaining image visibility.
 * The parameters listed here will be based on the input parameters provided for the polygonOutline.
 * @param offset Variable that holds the Offset(x, y) to move/ draw the bounding box from the current provided offset. By default shapes are drawn on the Canvas() starting (0, 0) [Top-left]
 * @param rotation Variable that holds the Rotation value for the bounding box
 * @param size Variable that holds the Size(x, y) of the polygon's edges
 * @param sides Variable that holds the number of sides for the polygon to be drawn
 */
internal fun calculateBoundingBoxDimensions(
    offset: Offset,
    rotation: Float,
    sides: Int = 0,
    size: Size
): BoundingBoxDims {
    val radius = if (size.width > size.height) size.width / 2f else size.height / 2f
    val cx = offset.x
    val cy = offset.y
    val r = rotation * (Math.PI / 180)
    val angle = 2.0 * Math.PI / sides

    var minX = Float.POSITIVE_INFINITY
    var minY = Float.POSITIVE_INFINITY
    var maxX = Float.NEGATIVE_INFINITY
    var maxY = Float.NEGATIVE_INFINITY
    var currentX: Float
    var currentY: Float

    Log.d("Bounding" , "$minX, $maxX, $minY, $maxY")
    if ( sides > 0 ){
        for (i in 0 until sides) {
            currentX = cx + (radius * cos(angle * i + r).toFloat())
            currentY = cy + (radius * sin(angle * i + r).toFloat())
            minX = kotlin.math.min(minX, currentX)
            maxX = kotlin.math.max(maxX, currentX)
            minY = kotlin.math.min(minY, currentY)
            maxY = kotlin.math.max(maxY, currentX)
        }
        Log.d("Bounding" , "$minX, $maxX, $minY, $maxY")
    } else {
        // All the other shapes
    }
    return BoundingBoxDims(minX, maxX, minY, maxY)
}

