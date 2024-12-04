package com.example.darbysyahtzee.composables

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun DiceRollerButton(
    onDiceRolled: () -> Unit,
    canRoll: Boolean,
    diceValues: List<Int>,
    diceHeld: List<Boolean>,
    onToggleHold: (Int) -> Unit,
    rollCount: Int
) {
    Button(
        onClick = {
            if (canRoll && rollCount < 3) {
                onDiceRolled()
            }
        },
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF9800)),
        enabled = canRoll && rollCount < 3,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = "Roll Dice",
            fontSize = 20.sp,
            color = Color.White,
            fontWeight = FontWeight.Bold
        )
    }

    Spacer(modifier = Modifier.height(16.dp))

    // Display the dice values and hold states
    DiceDisplay(
        diceValues = diceValues,
        diceHeld = diceHeld,
        onToggleHold = onToggleHold,
        isRolling = false
    )
}
