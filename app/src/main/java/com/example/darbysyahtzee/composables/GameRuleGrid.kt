package com.example.darbysyahtzee.composables

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun GameRulesGrid() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        //Lato sinistro
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            GameRuleRow(title = "UNO", score = "")
            GameRuleRow(title = "DUE", score = "")
            GameRuleRow(title = "TRE", score = "")
            GameRuleRow(title = "QUATTRO", score = "")
            GameRuleRow(title = "CINQUE", score = "")
            GameRuleRow(title = "SEI", score = "")
            GameRuleRow(title = "BONUS", score = "+35")
        }

        //Lato destro
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)
        ) {
            GameRuleRow(title = "TRIS", score = "")
            GameRuleRow(title = "POKER", score = "")
            GameRuleRow(title = "FULL HOUSE", score = "")
            GameRuleRow(title = "PICCOLA\nSCALA", score = "")
            GameRuleRow(title = "GRANDE\nSCALA", score = "")
            GameRuleRow(title = "YATZY", score = "")
            GameRuleRow(title = "CHANCE", score = "")
        }
    }
}

