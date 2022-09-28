package com.compose.playground.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProgressButton(
    navController: NavController
) {
    var currentPage by remember { mutableStateOf("A") }

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        com.compose.components.ProgressButton(
            progress = 0.3f,
            onClick = {
                currentPage = if (currentPage == "A") {
                    "B"
                } else {
                    "A"
                }
            },
            backgroundColor = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(200.dp)
                .height(56.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(200.dp)
                    .height(56.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Button",
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}