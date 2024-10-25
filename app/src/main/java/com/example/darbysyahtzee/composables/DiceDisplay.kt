package com.example.darbysyahtzee.composables

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.viewModels.getDiceImageResource

@Composable
fun DiceDisplay(
    diceValues: List<Int>,
    diceHeld: List<Boolean>,
    onToggleHold: (Int) -> Unit,
    isRolling: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        diceValues.forEachIndexed { index, diceValue ->
            val diceImageResource = when (diceValue) {
                1 -> R.drawable.dice_1
                2 -> R.drawable.dice_2
                3 -> R.drawable.dice_3
                4 -> R.drawable.dice_4
                5 -> R.drawable.dice_5
                6 -> R.drawable.dice_6
                else -> R.drawable.dice_1
            }

            // Display each dice with hold feature
            Image(
                painter = painterResource(id = diceImageResource),
                contentDescription = "Dice $diceValue",
                modifier = Modifier
                    .size(64.dp)
                    .clickable {
                        if (!isRolling) {
                            onToggleHold(index) // Toggle holding state of each dice
                        }
                    }
                    .border(2.dp, if (diceHeld[index]) Color.Green else Color.Transparent)
            )
        }
    }
}