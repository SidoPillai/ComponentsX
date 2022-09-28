package com.compose.playground.pages

import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController

@Composable
fun TimelineIntro(
    navController: NavController
) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        val configuration = LocalConfiguration.current
        val screenWidth = configuration.screenWidthDp
        var sliderPosition by remember { mutableStateOf(0f) }
        var shapeSize by remember { mutableStateOf(screenWidth) }

        Column(modifier = Modifier.padding(16.dp)) {
            Slider(
                value = sliderPosition,
                onValueChange = {
                    sliderPosition = it
                    Log.d("TimelineIntro", "$it")
                    shapeSize = (it * screenWidth).toInt()
                },
            )
            TimelineIntro(
                modifier = Modifier
                    .width(400.dp + (sliderPosition.dp.times(100)))
                    .height(600.dp + (sliderPosition.dp.times(100)))
                    .padding(20.dp)
                    .background(Color.Cyan),
                shapeSize = shapeSize.dp.div(8)
            )
        }
    }
}

@Composable
internal fun TimelineIntro(
    modifier: Modifier,
    shapeSize: Dp
) {
    Box(
        modifier = modifier
            .background(Color.White)
            .semantics(mergeDescendants = true) {},
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
            modifier = Modifier
                .fillMaxSize()
                .align(Alignment.TopStart)
                .padding(16.dp)
                .background(Color.White)
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.2f))
            Text(
                text = "title",
                fontSize = 24.sp,
                maxLines = 2,
                color = Color.Black,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "subtitle",
                fontSize = 16.sp,
                maxLines = 3,
                color = Color.Black.copy(alpha = 0.5f),
                overflow = TextOverflow.Ellipsis
            )
        }
        Block(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
//                .border(BorderStroke(width = 1.dp, color = Color.Red)) ,
            shapeSize = shapeSize
        )
    }
}

@Composable
internal fun Block(
    modifier: Modifier,
    shapeSize: Dp
) {
    val brush = Brush.linearGradient(0f to Color.Black.copy(alpha = 0.45f), 1000f to Color.Black.copy(alpha = 0.45f))
    val spacingFactor = (-10).dp

//    val shapeSize = maxWidth/8

    Column(
        modifier = modifier
//            .width(maxWidth.dp)
            .semantics(mergeDescendants = true, properties = {}),
        verticalArrangement = Arrangement.spacedBy(spacingFactor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
//                .width(maxWidth.dp)
                .padding(start = 88.dp)
//                .border(BorderStroke(width = 1.dp, color = Color.Green))
            ,
            horizontalArrangement = Arrangement.spacedBy(150.dp)
        ) {
            Column(
                modifier = Modifier
//                    .border(BorderStroke(width = 1.dp, color = Color.Blue))
                ,
                // .padding(start = 120.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(shapeSize)
//                        .width(IntrinsicSize.Min)
//                        .height(IntrinsicSize.Min)
//                        .size(IntrinsicSize.Min shapeSize.dp)
//                        .size(64.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 4.dp,
                            brush = brush,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.Black),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Canvas(
                    modifier = Modifier
                        .width(1.dp)
                        .height(120.dp)
                ) {
                    val height = size.height
                    val width = size.width
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = height),
                        color = Color.Black,
                        strokeWidth = 3.0f,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 10f, 10f, 10f), phase = 0f)
                    )
                }
//                Canvas(
//                    modifier = Modifier
//                        .fillMaxWidth(),
//                ) {
//                    drawLine(
//                        start = Offset(x = 0f, y = 0f),
//                        end = Offset(x = size.width, y = 0f),
//                        color = Color.Cyan,
//                        strokeWidth = 5F
//                    )
//                }
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding()
                        .zIndex(100f)
                        .background(Color.Red)
                        .size(18.dp)
                )
            }

            Column(
                modifier = Modifier
//                    .border(BorderStroke(width = 1.dp, color = Color.Blue))
                ,
                verticalArrangement = Arrangement.spacedBy(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
//                        .size(64.dp)
//                        .size(shapeSize.dp)
//                        .width(IntrinsicSize.Min)
//                        .height(IntrinsicSize.Min)
                        .size(shapeSize)
                        .clip(RoundedCornerShape(8.dp))
                        .border(
                            width = 4.dp,
                            brush = brush,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(Color.Black),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Canvas(
                    modifier = Modifier
                        .width(1.dp)
                        .height(120.dp)
                ) {
                    val height = size.height
                    val width = size.width
                    drawLine(
                        start = Offset(x = 0f, y = 0f),
                        end = Offset(x = 0f, y = height),
                        color = Color.Black,
                        strokeWidth = 3.0f,
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(10f, 10f, 10f, 10f), phase = 0f)
                    )
                }
                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding()
                        .zIndex(100f)
                        .background(Color.Red)
                        .size(18.dp)
                )
            }
        }

        Divider(
            modifier = Modifier
                .padding(start = 88.dp.plus(shapeSize / 2))
                .fillMaxWidth()
                .height(2.dp)
                .background(Color.Black)
                .zIndex(-1f)
        )

//        Canvas(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(300.dp)
//        ) {
//            val canvasWidth = size.width
//            val canvasHeight = size.height
//
//            drawLine(
//                start = Offset(x = canvasWidth, y = 0f),
//                end = Offset(x = 0f, y = canvasHeight),
//                color = Color.Cyan,
//                strokeWidth = 5F
//            )
//        }

        Spacer(modifier = Modifier.height(56.0.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    TimelineIntro(
        modifier = Modifier
            .width(400.dp)
            .height(1600.dp)
            .padding(20.dp)
            .background(Color.Cyan),
        shapeSize = screenWidth.dp.div(8)
    )
}