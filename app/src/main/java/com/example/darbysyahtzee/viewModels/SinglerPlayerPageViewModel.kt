package com.example.darbysyahtzee.viewModels

import androidx.compose.runtime.Composable
import com.example.darbysyahtzee.R

@Composable
fun getDiceImageResource(diceValue: Int): Int {
    return when (diceValue) {
        1 -> R.drawable.dice_1
        2 -> R.drawable.dice_2
        3 -> R.drawable.dice_3
        4 -> R.drawable.dice_4
        5 -> R.drawable.dice_5
        6 -> R.drawable.dice_6
        else -> R.drawable.dice_1
    }
}