package com.compose.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.compose.components.CrossSlide
import com.compose.components.ProgressButton
import com.compose.modifiers.NeuInsets
import com.compose.modifiers.dashedBorder
import com.compose.modifiers.neumorphic
import com.compose.modifiers.shapes.Punched
import com.compose.playground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var currentPage by remember { mutableStateOf("A") }

                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(start = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(30.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        item {
                            ShortStoriesCardView(
                                modifier = Modifier
                                    .width(160.dp)
                                    .height(240.dp)
                                    .dashedBorder(
                                        width = 2.dp,
                                        color = Color.Cyan,
                                        shape = MaterialTheme.shapes.medium, on = 4.dp, off = 4.dp
                                    )
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        item {
                            ProgressButton(
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

                        item {
                            CrossSlide(targetState = currentPage) { screen ->
                                when (screen) {
                                    "A" -> Text("Page A")
                                    "B" -> Text("Page B")
                                }
                            }
                        }

                        item {
                            Button(
                                onClick = { /*TODO*/ },
                                modifier = Modifier
                                    .padding(12.dp)
                                    .neumorphic(
                                        lightShadowColor = Color.White,
                                        darkShadowColor = Color.Gray.copy(alpha = 0.3f),
                                        elevation = 4.dp,
                                        strokeWidth = 6.dp,
                                        neuInsets = NeuInsets(1.dp, 1.dp)
                                    ),
                            ) {
                                Text(
                                    text = "Button",
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }

                        item {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor =  MaterialTheme.colorScheme.surfaceVariant,
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .padding(16.dp)
                                    .size(250.dp, 60.dp)
                                    .neumorphic(
                                        neuShape = Punched.Rounded(radius = 8.dp)
                                    )
                            ) {
                                Text(
                                    modifier = Modifier.fillMaxSize(),
                                    text = "Neomorphic Button",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShortStoriesCardView(
    modifier: Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(CornerSize(8.dp)),
    ) {
        Box(
            modifier = Modifier.height(240.dp)
        ) {
            Image(
                contentDescription = null,
                painter = painterResource(id = R.drawable.thumbnail_stock),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.8f)
                            ),
                            startY = 100f,
                            endY = 0f
                        )
                    )
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.TopStart
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .zIndex(1f)
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Image(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(CircleShape),
                        contentDescription = null,
                        painter = painterResource(id = R.drawable.thumbnail_stock),
                        contentScale = ContentScale.Crop
                    )

                    Text(
                        text = "livvykemp",
                        color = Color.White

                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.5f)
                            ),
                            startY = 200f
                        )
                    )
            )



            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(fontWeight = FontWeight.W700, color = Color.White)
                        ) {
                            append("Jetpack Compose Playground")
                        }
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShortStoriesCardView_Preview() {
    ShortStoriesCardView(
        modifier = Modifier
            .width(160.dp)
            .height(240.dp)
    )
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