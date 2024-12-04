package com.example.darbysyahtzee.database

data class Game(
    val gameId: String,           // Unique ID for each game
    val date: String,             // Date when the game was completed
    val finalScore: Int,          // Final score of the game
    val scores: List<Int>         // List of scores for each turn
)
