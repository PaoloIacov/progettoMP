package com.example.darbysyahtzee.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameRuleRow(title: String, score: String, initialValue: Int = 0) {
    // State to store the current value of the button
    var currentValue by remember { mutableStateOf(initialValue) }

    // Remember the interaction source to avoid recomposition warnings
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(8.dp))
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Display the title of the rule
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 12.sp
        )

        // Display the current score
        Text(
            text = score,
            style = MaterialTheme.typography.bodyLarge,
            fontSize = 12.sp
        )

        // Transparent, square button without ripple effect
        Box(
            modifier = Modifier
                .size(40.dp) // Makes the button square
                .background(Color.Transparent) // Transparent background
                .border(1.dp, Color.Gray, RoundedCornerShape(0.dp)) // Optional border for structure
                .clickable(
                    interactionSource = interactionSource, // Remembered interaction source
                    indication = null // No visual indication on click
                ) {
                    currentValue++ // Increment the value on click
                },
            contentAlignment = Alignment.Center
        ) {
            Text(text = "$currentValue", fontSize = 18.sp)
        }
    }
}
