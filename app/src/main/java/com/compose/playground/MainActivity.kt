package com.compose.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compose.components.ProgressButton
import com.compose.components.ShareScreenScaffold
import com.compose.playground.pages.*
import com.compose.playground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Navigation()
                }
            }
        }
    }
}

@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            Home(navController = navController)
        }
        composable("Horizontal Pager") {
            HorizontalPager(navController = navController)
        }
        composable("Vertical Pager") {
            VerticalPager(navController = navController)
        }
        composable("Neomorphic Button") {
            NeomorphicButton(navController = navController)
        }
        composable("Progress Button") {
            com.compose.playground.pages.ProgressButton(navController = navController)
        }
        composable("CrossSlide") {
            CrossSlide(navController = navController)
        }
        composable("Card View") {
            com.compose.playground.pages.Card(navController = navController)
        }
        composable("Timeline Intro") {
            TimelineIntro(navController = navController)
        }
        composable("Share Card"){
            ShareScreenScaffold()
        }
    }
}



@Preview
@Composable
fun ProgressButton_Preview() {
    ProgressButton(
        progress = 0.3f,
        onClick = {},
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
                color = Color.White
            )
        }
    }
}