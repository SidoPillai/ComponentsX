@file:OptIn(ExperimentalMaterialApi::class)
package com.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.*
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.smarttoolfactory.screenshot.ScreenshotBox
import com.smarttoolfactory.screenshot.ScreenshotState
import com.smarttoolfactory.screenshot.rememberScreenshotState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.random.Random

/**
 * Data class defining the structure of Sample data objects
 */
private data class TestDataMessages(
    val index: Int,
    val imageUrl: String,
    val author: String,
    val body: String
)

/**
 * SampleData object that returns mock data to be used in this file by various composables
 */
private object SampleData {
    fun getDataAtIndex(index: Int): TestDataMessages {
        return conversationSample[index];
    }

    private val conversationSample = listOf(
        TestDataMessages(
            0,
            "https://images.freeimages.com/images/large-previews/d0f/nuclear-power-plant-1314782.jpg",
            "User 1",
            "Test...Test...Test..."
        ),
        TestDataMessages(
            1,
            "https://images.freeimages.com/images/large-previews/849/rancho-seco-nuclear-power-plant-1-1395317.jpg",
            "User 2",
            "Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit" +
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit" +
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit Lorem ipsum dolor sit amet, consectetur adipiscing elit"
        ),
        TestDataMessages(
            2,
            "https://images.freeimages.com/images/large-previews/cc3/folks-land-1-1203476.jpg",
            "User 3",
            " sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        ),
        TestDataMessages(
            3,
            "https://images.freeimages.com/images/large-previews/a20/our-country-1401255.jpg",
            "User 4",
            "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat" +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat" +
                    "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat"
        ),
        TestDataMessages(
            4,
            "https://images.freeimages.com/images/large-previews/457/suwalszczyzna-poland-1387583.jpg",
            "User 5",
            "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur" +
                    "Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur"
        ),
        TestDataMessages(
            5,
            "https://images.freeimages.com/images/large-previews/6be/winter-lake-1513222.jpg",
            "User 6",
            "Excepteur sint occaecat cupidatat non proident"
        ),
        TestDataMessages(
            6,
            "https://images.freeimages.com/images/large-previews/338/sunset-over-lake-2-1377767.jpg",
            "User 7",
            "sunt in culpa qui officia deserunt mollit anim id est laborum."
        ),
        TestDataMessages(
            7,
            "https://images.freeimages.com/images/large-previews/fec/sunset-rays-1391805.jpg",
            "User 8",
            "Elit eget gravida cum sociis natoque penatibus et magnis dis. Risus quis varius quam quisque id diam vel quam elementum"
        ),
        TestDataMessages(
            8,
            "https://images.freeimages.com/images/large-previews/e0c/hawaiin-sunset-1368289.jpg",
            "User 9",
            " Amet mauris commodo quis imperdiet massa tincidunt nunc pulvinar"
        ),
        TestDataMessages(
            9,
            "https://images.freeimages.com/images/large-previews/51c/hawaii-3-1531124.jpg",
            "User 10",
            "Neque ornare aenean euismod elementum. Leo in vitae turpis massa sed elementum tempus."
        )
    )
}

/**
 * The wrapper page to display the ShareScreen() composable with a bottom scaffold and provides the required state variables
 */
@Composable
@Stable
fun ShareScreenScaffold() {
    val currentData = SampleData.getDataAtIndex(Random.nextInt(from = 0, until = 9))
    val screenshotState = rememberScreenshotState()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = BottomSheetState(
        BottomSheetValue.Collapsed))
    val scope = rememberCoroutineScope()

    BottomSheetScaffold(
        scaffoldState = bottomSheetScaffoldState,
        content = {
            ShareScreen(
                currentData = currentData,
                screenshotState = screenshotState,
                bottomSheetScaffoldState = bottomSheetScaffoldState,
                scope = scope
            )
        },
        sheetContent =  {
            Column(
                modifier = Modifier
                    .background(Color.DarkGray)
                    .fillMaxWidth()
                    .height(LocalConfiguration.current.screenHeightDp.dp - 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Box (
                    Modifier
                        .width(100.dp)
                        .height(6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.Black)
                )
                
                Spacer(modifier = Modifier.weight(1.0f))

                screenshotState.imageBitmap?.let {
                    Image(
                        modifier = Modifier
                            .width(400.dp)
                            .height(400.dp),
                        bitmap = it,
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.weight(1.0f))
            }
        },
        sheetPeekHeight = 0.dp,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetBackgroundColor = Color.White,
    )
}

/**
 * A full-screen composable that is used as the container page to display/ use ShareCardDefault() or ShareCardCustom()
 * @param currentData The data object that is passed from the View-model
 * @param screenshotState State variable used to keep track of the ScreenshotBox composable to take composable screenshots
 * @param bottomSheetScaffoldState State variable used to maintain the bottom sheet which is used to display the screenshotted bitmap image
 * @param scope Coroutine scope used to open/ close the Bottom sheet
 */
@ExperimentalMaterialApi
@Composable
@Stable
private fun ShareScreen(
    currentData: TestDataMessages,
    screenshotState: ScreenshotState,
    bottomSheetScaffoldState: BottomSheetScaffoldState,
    scope: CoroutineScope
) {
    Column(
        modifier = Modifier.background(Color.White)
    ){
        Row(
            modifier = Modifier
                .padding(start = 20.dp, top = 10.dp, end = 5.dp, bottom = 10.dp)
                .fillMaxWidth()
                .weight(1f)
        ) {


            Spacer (Modifier.weight(1f))

            OutlinedIconButton(
                onClick = { },
                colors = IconButtonDefaults.iconButtonColors(),
                enabled = true,
                border = IconButtonDefaults.outlinedIconButtonBorder(enabled = true)
            ) {
                Icon(
                    painter = painterResource(id = com.google.android.material.R.drawable.abc_ic_menu_overflow_material),
                    contentDescription = "More Icon",
                    tint = Color.Black
                )
            }

            OutlinedIconButton(
                onClick = {
                    screenshotState.capture()
                    scope.launch {
                        if (bottomSheetScaffoldState.bottomSheetState.isCollapsed){
                            bottomSheetScaffoldState.bottomSheetState.expand()
                        } else{
                            bottomSheetScaffoldState.bottomSheetState.collapse()
                        }
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(),
                enabled = true,
                border = IconButtonDefaults.outlinedIconButtonBorder(enabled = true)
            ) {
                Icon(
                    painter = painterResource(id = androidx.appcompat.R.drawable.abc_ic_menu_share_mtrl_alpha),
                    contentDescription = "Share button",
                    tint = Color.Black
                )
            }
        }

        ScreenshotBox(
            screenshotState = screenshotState,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .widthIn(100.dp, 320.dp)
                .heightIn(100.dp, LocalConfiguration.current.screenHeightDp.dp - 100.dp)
                .align(Alignment.CenterHorizontally)
        ){
            ShareCardCustom(
                header = { CardHeader(currentData.author, currentData.imageUrl, Color.White, MaterialTheme.typography.labelMedium) },
                content = { CardContent(currentData.body, Color.White, MaterialTheme.typography.bodyMedium) },
                media = { CardMedia(currentData.imageUrl) },
                modifier = Modifier
                    .padding(1.dp),
                cardColors = CardDefaults.cardColors(
                    containerColor = Color.Black
                ),
                cardElevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp,
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}

/**
 * A default card header UI composable that is used in ShareCardDefault() and ShareCardCustom()
 * @param title The `title` text to be displayed
 * @param titleImageUri Url of the`image` to be displayed in the title header
 * @param headerTextColor Text color of the header
 * @param headerTextStyle Text style of the header
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Stable
private fun CardHeader(
    title: String,
    titleImageUri: String,
    headerTextColor: Color,
    headerTextStyle: TextStyle
){
    Row(
        modifier = Modifier
            .height(50.dp)
            .fillMaxWidth()
    ) {
        GlideImage(
            model = titleImageUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
                .size(40.dp)
                .clip(CircleShape)
                .border(
                    1.dp,
                    Color.Black,
                    CircleShape
                )
        )

        Spacer(modifier = Modifier.width(2.dp))

        Text(
            text = title,
            modifier = Modifier
                .align(Alignment.CenterVertically),
            color = headerTextColor,
            style = headerTextStyle
        )

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            painter = painterResource(androidx.appcompat.R.drawable.abc_ic_menu_share_mtrl_alpha),
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterVertically)
        )
    }
}

/**
 * A default card content UI composable that is used in ShareCardDefault() and ShareCardCustom()
 * @param description The `contentDescription` text to be displayed
 * @param contentTextColor Text color of the content
 * @param contentTextStyle Text style of the content
 */
@Composable
@Stable
private fun CardContent(
    description: String,
    contentTextColor: Color,
    contentTextStyle: TextStyle
){
    Text(
        modifier = Modifier
            .padding(10.dp)
            .wrapContentSize(),
        text = description,
        color = contentTextColor,
        textAlign = TextAlign.Left,
        maxLines = 10,
        overflow = TextOverflow.Ellipsis,
        style = contentTextStyle
    )
}

/**
 * A default card media UI composable that is used in ShareCardDefault() and ShareCardCustom()
 * @param contentImageUri Url of the`image` to be displayed
 */
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
@Stable
private fun CardMedia(
    contentImageUri: String
){
    Card(
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier
            .padding(10.dp)
            .height(150.dp)
            .fillMaxWidth()

    ) {
        GlideImage(
            model = contentImageUri,
            contentDescription = "Content Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}


/**
 * A composable that returns a reusable card widget with default UI composables (Here CardHeader(), CardContent() and CardMedia() composables)
 * @param title Title text to be displayed in the card header.
 * @param titleImageUri Media to be displayed as part of the card title content.
 * @param description Description text to be displayed in the card body.
 * @param mediaUri Media to be displayed as part of the card body content.
 */
@Composable
@Stable
internal fun ShareCardDefault(
    title: String,
    titleImageUri: String,
    description: String,
    mediaUri: String
){
    ShareCardCustom(
        header = { CardHeader(title, titleImageUri, Color.White, MaterialTheme.typography.labelMedium) },
        content = { CardContent(description, Color.White, MaterialTheme.typography.bodyMedium) },
        media = { CardMedia(mediaUri) },
        modifier = Modifier
            .padding(1.dp),
        cardColors = CardDefaults.cardColors(
            containerColor = Color.Black
        ),
        cardElevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp,
        )
    )
}


/**
 * A composable that returns a reusable card widget with user-defined [header], [content] and [media]
 * @param modifier Any custom modifier for the ShareCardCustom() composable
 * @param header Composable with the respective header content and UI.
 * @param content Composable with the respective content body.
 * @param media Composable with the respective media content.
 * @param cardColors CardColors to customize the container and content color of the Card on various states
 * @param cardElevation CardElevation to customize the elevation of the Card on various states
 */
@Composable
@Stable
internal fun ShareCardCustom(
    modifier: Modifier = Modifier,
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit,
    media: @Composable () -> Unit = {},
    cardColors: CardColors = CardDefaults.cardColors(),
    cardElevation: CardElevation = CardDefaults.cardElevation()
) {
    Card(
        modifier = Modifier.then(modifier),
        colors = cardColors,
        elevation = cardElevation
    ){
        ShareCardLayout {
            header()
            content()
            media()
        }
    }
}

/**
 * Layout function used by SharedCardDefault() and ShareCardCustom() composables to measure, place and
 * build a custom layout
 * @param content Composable with the respective content body.
 */
@Composable
@Stable
private fun ShareCardLayout(
    content: @Composable () -> Unit
) {
    var height: Int
    var width: Int

    Layout(
        content = content
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        height = placeables.sumOf { it.height }.coerceAtMost(constraints.maxHeight)
        width = placeables.maxOf { it.width }.coerceAtMost(constraints.maxWidth)

        layout(width, height) {
            var yPosition = 0
            placeables.forEach { placeable ->
                placeable.placeRelative(x = 0, y = yPosition)
                yPosition += placeable.height
            }
        }
    }
}