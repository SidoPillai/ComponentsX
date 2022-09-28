package com.compose.playground.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.compose.modifiers.neumorphic
import com.compose.modifiers.shapes.Pot
import com.compose.modifiers.shapes.Pressed
import com.compose.modifiers.shapes.Punched

@Composable
fun NeomorphicButton(
    navController: NavController
) {
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
    ) {
        item {
            androidx.compose.material3.Card(
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

        item {
            androidx.compose.material3.Card(
                colors = CardDefaults.cardColors(
                    containerColor =  MaterialTheme.colorScheme.surfaceVariant,
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .size(250.dp, 60.dp)
                    .neumorphic(
                        neuShape = Pot.Rounded(radius = 8.dp)
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

        item {
            androidx.compose.material3.Card(
                colors = CardDefaults.cardColors(
                    containerColor =  MaterialTheme.colorScheme.surfaceVariant,
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .size(250.dp, 60.dp)
                    .neumorphic(
                        neuShape = Pressed.Rounded(radius = 8.dp)
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