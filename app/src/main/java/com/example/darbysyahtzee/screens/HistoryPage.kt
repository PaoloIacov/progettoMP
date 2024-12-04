package com.example.darbysyahtzee.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.darbysyahtzee.composables.GameCard

// Sample data model for Game
data class Game(
    val gameId: String,
    val date: String,
    val finalScore: Int,
    val scores: List<Int> // List of scores for each turn
)

// Mock list of games - replace this with actual data fetching
val gameHistory = List(10) { index ->
    Game(gameId = "game_$index", date = "2024-10-${15 - index}", finalScore = (100..300).random(), scores = List(13) { (10..50).random() })
}

@Composable
fun HistoryPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Game History",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(gameHistory.size) { index ->
                val game = gameHistory[index]
                GameCard(game = game) {
                    navController.navigate("GameDetail/${game.gameId}")
                }
            }
        }
    }
}

