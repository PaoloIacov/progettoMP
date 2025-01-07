package com.example.darbysyahtzee.composables

import java.time.LocalDateTime

data class Game(
    val gameId: String,
    val date: LocalDateTime,
    val finalScore: Int,
    val scores: List<TurnScoreDetail>,
    val bonusAchieved: Boolean
)
