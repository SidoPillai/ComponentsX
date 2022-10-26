package com.compose.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import com.compose.components.BlendModeDemoType.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Enum class that specifies the types of BlendModes to switch between, showing different transformations for the shapes/ geometry on the Canvas()
 */
internal enum class BlendModeDemoType {
    Src, Dst, SrcIn, DstIn, SrcOver, DstOver, ThreeBodyType1, ThreeBodyType2, ThreeBodyType3, DrawImage, Dodge, Burn
}

/**
 * Composable function that displays the main page content and the bottom sheet scaffold to switch between different blend modes
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Stable
fun BlendModesDemo(){
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = BottomSheetState(
            BottomSheetValue.Collapsed
        )
    )

    val scope = rememberCoroutineScope()
    var currentBlendMode by remember { mutableStateOf(Src) }

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        content = { BlendModesMainContent(
            blendMode = currentBlendMode,
            bottomSheetScaffoldState = bottomSheetScaffoldState,
            scope = scope
        ) },
        sheetContent =  {
            BlendModesSheetContent(
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                onBlendModeChanged = {
                    currentBlendMode = it
                },
                scope = scope
            )
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = MaterialTheme.colorScheme.surface
    )
}

/**
 * Main content page which contains a canvas to display the shapes and blend modes and a settings page for switching between different blend modes
 * @param blendMode Variable that holds the current blend mode being used for the shapes drawn on the Canvas()
 * @param bottomSheetScaffoldState Variable that holds the current bottomSheetState for collapse/ expand operations
 * @param scope Variable that holds the current coroutine scope to launch state transitions
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Stable
internal fun BlendModesMainContent(
    blendMode: BlendModeDemoType,
    bottomSheetScaffoldState : BottomSheetScaffoldState,
    scope: CoroutineScope
){
    val imageBitmapSource = ImageBitmap.imageResource(
        LocalContext.current.resources,
        R.drawable.image
    )

    val imageBitmap by remember { mutableStateOf(imageBitmapSource) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .height(40.dp)
                .background(MaterialTheme.colorScheme.surface)
        ){
            Spacer(modifier = Modifier.weight(1f))

            OutlinedIconButton(
                onClick = {
                    toggleBottomSheet(
                        bottomSheetScaffoldState = bottomSheetScaffoldState,
                        scope = scope
                    )
                },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                enabled = true,
                border = IconButtonDefaults.outlinedIconButtonBorder(enabled = true)
            ) {
                Icon(
                    imageVector = Icons.Filled.Settings,
                    contentDescription = "Edit blend modes icon"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(600.dp)
                .graphicsLayer {
                    alpha = .99f
                },
            onDraw = {
                drawCircle(
                    color = Color.Green,
                )

                when (blendMode){
                    Src -> {
                        drawRect(
                            blendMode = BlendMode.Src,
                            color = Color.Red,
                        )
                    }
                    Dst -> {
                        drawRect(
                            blendMode = BlendMode.Dst,
                            color = Color.Red,
                        )
                    }
                    SrcIn -> {
                        drawRect(
                            blendMode = BlendMode.SrcIn,
                            color = Color.Red,
                        )
                    }
                    DstIn -> {
                        drawRect(
                            blendMode = BlendMode.DstIn,
                            color = Color.Red,
                        )
                    }
                    SrcOver -> {
                        drawRect(
                            blendMode = BlendMode.SrcOver,
                            color = Color.Red,
                        )
                    }
                    DstOver -> {
                        drawRect(
                            blendMode = BlendMode.DstOver,
                            color = Color.Red,
                        )
                    }
                    ThreeBodyType1 -> {
                        drawRect(
                            color = Color.Red
                        )
                        drawOutline(
                            blendMode = BlendMode.SrcIn,
                            color = Color.Black,
                            outline = polygonOutline(
                                offset = Offset(500f, 800f),
                                rotation = 0f,
                                size = Size(600f, 600f),
                                sides = 6
                            ),
                        )
                    }
                    ThreeBodyType2 -> {
                        drawRect(
                            blendMode = BlendMode.SrcIn,
                            color = Color.Red
                        )
                        drawOutline(
                            blendMode = BlendMode.SrcIn,
                            color = Color.Black,
                            outline = polygonOutline(
                                offset = Offset(500f, 800f),
                                rotation = 0f,
                                size = Size(600f, 600f),
                                sides = 6
                            ),
                        )
                    }
                    ThreeBodyType3 -> {
                        drawRect(
                            blendMode = BlendMode.DstIn,
                            color = Color.Red
                        )
                        drawOutline(
                            blendMode = BlendMode.SrcIn,
                            color = Color.Black,
                            outline = polygonOutline(
                                offset = Offset(500f, 800f),
                                rotation = 0f,
                                size = Size(600f, 600f),
                                sides = 6
                            ),
                        )
                    }
                    DrawImage -> {
                        drawImage(
                            blendMode = BlendMode.SrcIn,
                            image = imageBitmap
                        )
                    }
                    Dodge -> {
                        drawImage(
                            blendMode = BlendMode.SrcIn,
                            image = imageBitmap,
                            colorFilter = ColorFilter.tint(
                                color = Color(0xADFFAA2E),
                                blendMode = BlendMode.ColorDodge
                            )
                        )
                    }
                    Burn -> {
                        drawImage(
                            blendMode = BlendMode.SrcIn,
                            image = imageBitmap,
                            colorFilter = ColorFilter.tint(
                                color = Color(0xADFFAA2E),
                                blendMode = BlendMode.ColorBurn
                            )
                        )
                    }
                }
            }
        )
    }
}

/**
 * Reusable function that triggers the bottomSheet's current state between Collapsed/ Expanded states
 * @param bottomSheetScaffoldState Variable that holds the current bottomSheetState for collapse/ expand operations
 * @param scope Variable that holds the current coroutine scope to launch state transitions
 */
@OptIn(ExperimentalMaterialApi::class)
internal fun toggleBottomSheet(
    bottomSheetScaffoldState : BottomSheetScaffoldState,
    scope: CoroutineScope
){
    scope.launch {
        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
            bottomSheetScaffoldState.bottomSheetState.expand()
        } else{
            bottomSheetScaffoldState.bottomSheetState.collapse()
        }
    }
}

/**
 * Sheet content page which displays a column of TextButton() to choose between different blend modes
 * @param bottomSheetScaffoldState Variable that holds the current bottomSheetState for collapse/ expand operations
 * @param onBlendModeChanged Lambda that changes the current blend mode being used
 * @param scope Variable that holds the current coroutine scope to launch state transitions
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
@Stable
internal fun BlendModesSheetContent(
    bottomSheetScaffoldState : BottomSheetScaffoldState,
    onBlendModeChanged: (BlendModeDemoType) -> Unit,
    scope: CoroutineScope
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        TextButton(
            onClick = {
                onBlendModeChanged(Src)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Source",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(Dst)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Destination",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(SrcIn)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Source In",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(DstIn)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Destination In",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(SrcOver)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Source Over",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(DstOver)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Destination Over",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(ThreeBodyType1)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "ThreeBodyType1",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(ThreeBodyType2)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "ThreeBodyType2",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(ThreeBodyType3)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "ThreeBodyType3",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(DrawImage)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Draw Image",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(Dodge)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Dodge",
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        TextButton(
            onClick = {
                onBlendModeChanged(Burn)
                toggleBottomSheet(
                    bottomSheetScaffoldState = bottomSheetScaffoldState,
                    scope = scope
                )
            }
        ) {
            Text(
                text = "Burn",
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}