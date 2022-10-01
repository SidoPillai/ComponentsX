package com.compose.playground.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

val routes = mutableListOf(
    "Horizontal Pager",
    "Vertical Pager",
    "Neomorphic Button",
    "Progress Button",
    "CrossSlide",
    "Card View",
    "Timeline Intro",
    "Share Card"
)

@Composable
fun Home(
    navController: NavController
){
    LazyGridFor(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        items = routes,
        columns = 2,
        navController = navController
    )
}

@Composable
fun <T> LazyGridFor(
    modifier: Modifier,
    items: List<T>,
    columns: Int = 1,
    navController: NavController
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(columns),
        modifier = Modifier.then(modifier),
        horizontalArrangement = Arrangement.Center
    ) {
        items(items.size) { index ->
            androidx.compose.material3.Card(
                modifier = Modifier.padding(8.dp),
                shape = RoundedCornerShape(CornerSize(8.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp,
                )
            ) {
                Column(
                    modifier = Modifier
                        .wrapContentSize()
                        .height(100.dp)
                        .fillMaxWidth()
                        .padding(5.dp)
                        .clickable {
                            navController.navigate(routes[index])
                        },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = items[index].toString(),
                        fontSize = 16.sp,
                        maxLines = 3,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        overflow = TextOverflow.Ellipsis,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Preview("LazyGridFor: example")
@Composable
fun LazyGridForPreview() {
    LazyGridFor(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        items = routes,
        columns = 2,
        navController = rememberNavController()
    )
}