package com.compose.playground.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.components.pager.Pager
import com.compose.components.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun VerticalPager(
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
            modifier = androidx.compose.ui.Modifier
                .width(screenWidth.dp)
                .height(screenWidth.dp),
            itemFraction = 1f,
            orientation = Orientation.Vertical,
            overshootFraction = 1f,
            initialIndex = 0,
            itemSpacing = 16.dp,
            contentFactory = { item ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(item),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.toString(),
                        modifier = Modifier.padding(all = 16.dp),
                        style = MaterialTheme.typography.labelMedium,
                        color = Color.Black
                    )
                }
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