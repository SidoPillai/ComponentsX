package com.compose.playground.pages

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.components.pager.Pager
import com.compose.components.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HorizontalPager(
    navController: NavController
) {
    val items = listOf(
        Color.Red,
        Color.Blue,
        Color.Green,
        Color.Yellow,
        Color.Cyan,
        Color.Magenta
    )
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp
    val pagerState = rememberPagerState()
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        val coroutineScope = rememberCoroutineScope()
        Pager(
            items = items,
            modifier = Modifier
                .width(screenWidth.dp)
                .height(screenWidth.dp.times(1.77f)),
            itemFraction = 1f,
            overshootFraction = 1f,
            initialIndex = 0,
            itemSpacing = 16.dp,
            contentFactory = { item ->
                TransitionItem(color = item)
            },
            state = pagerState
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    pagerState.animateScrollToPage(page = 2)
                }
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Scroll to the third page")
        }
    }
}

@Stable
@Composable
private fun TransitionItem(color: Color) {
    var visible by remember { mutableStateOf(false) }
    var currentTime by remember { mutableStateOf(2000L) }
    val isTimerRunning by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
        if (currentTime > 0 && isTimerRunning) {
            delay(100L)
            currentTime -= 100L
        } else {
            visible = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.BottomCenter),
            visible = visible,
            enter = slideInHorizontally(
                initialOffsetX = { 600 },
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearEasing
                )
            )
        ) {
            Text(
                text = color.toString(),
                modifier = Modifier.padding(all = 16.dp),
                style = MaterialTheme.typography.displayLarge,
                color = Color.Black.copy(alpha = 0.5f)
            )
        }
    }
}
