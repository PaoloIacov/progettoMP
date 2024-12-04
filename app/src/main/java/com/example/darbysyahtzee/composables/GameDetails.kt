package com.example.darbysyahtzee.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun GameDetailPage(gameId: String, navController: NavController) {
    // Mock data for demonstration - replace with actual game details fetching
    val gameDetails = getGameDetails(gameId)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Game Details",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Game ID: $gameId", fontSize = 18.sp)
        Text(text = "Final Score: ${gameDetails?.finalScore ?: "N/A"}", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(24.dp))
        gameDetails?.scores?.forEachIndexed { index, score ->
            Text(text = "Turn ${index + 1}: $score points", fontSize = 16.sp)
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { navController.navigate("home") }) {
            Text("Return to Home")
        }
    }
}

// Mock function for fetching game details by ID
fun getGameDetails(gameId: String): GameDetails? {
    return GameDetails(
        finalScore = (100..300).random(),
        scores = List(13) { (10..50).random() }
    )
}

// Data class for detailed game info
data class GameDetails(val finalScore: Int, val scores: List<Int>)
