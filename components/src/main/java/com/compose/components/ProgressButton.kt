package com.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ProgressButton(
    progress: Float,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color,
    content: @Composable RowScope.() -> Unit
) {
    var height by remember { mutableStateOf(0.dp) }

    val shape = RoundedCornerShape(8.dp)
    val density = LocalDensity.current
    Box(
        modifier = modifier
            .background(
                color = backgroundColor,
                shape = shape,
            )
            .clip(shape),
    ) {
        Button(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    height = density.run { it.height.toDp() }
                },
            contentPadding = PaddingValues(16.dp),
            onClick = onClick,
            content = content
        )
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .height(height),
            progress = progress,
            trackColor = Color.Transparent,
            color = Color.White.copy(alpha = 0.15f),
        )
    }
}