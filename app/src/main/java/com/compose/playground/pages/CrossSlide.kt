package com.compose.playground.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CrossSlide(
    navController: NavController
) {
    var currentPage by remember { mutableStateOf("A") }

    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            com.compose.components.CrossSlide(targetState = currentPage) { screen ->
                when (screen) {
                    "A" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                text = "Page A",
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                    "B" -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .background(MaterialTheme.colorScheme.secondary)
                        ) {
                            Text(
                                text = "Page B",
                                color = MaterialTheme.colorScheme.onSecondary
                            )
                        }
                    }
                }
            }
        }

        item {
            Button(
                onClick = {
                    currentPage = if (currentPage == "A") {
                        "B"
                    } else {
                        "A"
                    }
                },
            ) {
                Text(
                    text = "Button",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}