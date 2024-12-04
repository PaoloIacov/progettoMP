package com.example.darbysyahtzee.composables

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.darbysyahtzee.screens.Game

@Composable
fun GameCard(game: Game, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Game ID: ${game.gameId}", fontSize = 16.sp)
            Text(text = "Date: ${game.date}", fontSize = 14.sp)
            Text(text = "Final Score: ${game.finalScore}", fontSize = 14.sp)
        }
    }
}

data class Game(
    val gameId: String,
    val date: String,
    val finalScore: Int,
    val scores: List<Int> // List of scores for each turn
)
