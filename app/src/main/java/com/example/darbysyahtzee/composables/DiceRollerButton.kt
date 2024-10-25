package com.example.darbysyahtzee.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.ui.theme.titleFont
import kotlinx.coroutines.delay


@Composable
fun DiceRollerButton(
    onDiceRolled: (List<Int>) -> Unit,
    canRoll: Boolean,
    diceValues: List<Int>,
    diceHeld: List<Boolean>,
    onToggleHold: (Int) -> Unit,
    rollCount: Int
) {
    var isRolling by remember { mutableStateOf(false) }

    // Button to roll dice
    Button(
        onClick = {
            if (canRoll && rollCount < 3) {
                isRolling = true // Trigger rolling animation if allowed
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
        enabled = canRoll && rollCount < 3, // Disable the button after 3 rolls
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = stringResource(R.string.roll_dice),
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Dice rolling animation logic
    if (isRolling) {
        LaunchedEffect(Unit) {
            // Simulate rolling dice animation
            for (i in 1..10) {
                onDiceRolled(List(5) { (1..6).random() }) // Update dice values randomly
                delay(100) // Animation delay between rolls
            }
            isRolling = false
        }
    }

    // Display the dice values
    DiceDisplay(
        diceValues = diceValues,
        diceHeld = diceHeld,
        onToggleHold = onToggleHold,
        isRolling = isRolling
    )
}