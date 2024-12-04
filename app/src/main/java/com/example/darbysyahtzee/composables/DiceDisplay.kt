package com.example.darbysyahtzee.composables

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import com.example.darbysyahtzee.R

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape

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
                else -> null // Nessuna immagine quando il valore è 0
            }

            // Box grigio scuro dietro al dado per diminuire la saturazione del nero
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(8.dp)) // Bordi arrotondati
                    .background(Color(0x41333333)) // Grigio scuro, meno saturo
                    .padding(4.dp)
                    .clickable {
                        if (!isRolling) {
                            onToggleHold(index)
                        }
                    }
            ) {
                diceImageResource?.let {
                    Image(
                        painter = painterResource(id = diceImageResource),
                        contentDescription = "Dice $diceValue",
                        modifier = Modifier
                            .size(64.dp)
                            .border(2.dp, if (diceHeld[index]) Color.Green else Color.Transparent)
                    )
                }
            }
        }
    }
}
