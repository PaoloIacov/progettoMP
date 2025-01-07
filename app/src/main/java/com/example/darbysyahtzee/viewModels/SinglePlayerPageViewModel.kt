package com.example.darbysyahtzee.viewModels

import LocalDateTimeAdapter
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.darbysyahtzee.composables.Game
import com.example.darbysyahtzee.composables.ScoreOption
import com.example.darbysyahtzee.composables.TurnScoreDetail
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

class SinglePlayerPageViewModel(private val context: Context) : ViewModel() {
    private val maxTurns = 13
    private val maxRollsPerTurn = 3
    private val bonusThreshold = 63
    private val bonusScore = 35
    private var isBonusAwarded = false

    private val gson = Gson()
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("game_history", Context.MODE_PRIVATE)

    // State properties
    private val _diceValues = MutableStateFlow(List(5) { 0 })
    val diceValues: StateFlow<List<Int>> = _diceValues

    private val _heldDice = MutableStateFlow(List(5) { false })
    val heldDice: StateFlow<List<Boolean>> = _heldDice

    private val _selectedOption = MutableStateFlow<ScoreOption?>(null)
    val selectedOption: StateFlow<ScoreOption?> = _selectedOption

    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore

    private val _turnCount = MutableStateFlow(0)
    val turnCount: StateFlow<Int> = _turnCount

    private val _canRoll = MutableStateFlow(true)
    val canRoll: StateFlow<Boolean> = _canRoll

    private val _rollsRemaining = MutableStateFlow(maxRollsPerTurn)
    val rollsRemaining: StateFlow<Int> = _rollsRemaining

    private val _minorScoreOptions = MutableStateFlow<List<ScoreOption>>(emptyList())
    val minorScoreOptions: StateFlow<List<ScoreOption>> = _minorScoreOptions

    private val _majorScoreOptions = MutableStateFlow<List<ScoreOption>>(emptyList())
    val majorScoreOptions: StateFlow<List<ScoreOption>> = _majorScoreOptions

    private val _isGameOver = MutableStateFlow(false)
    val isGameOver: StateFlow<Boolean> = _isGameOver

    private val _bonusPoints = MutableStateFlow(bonusScore)
    val bonusPoints: StateFlow<Int> = _bonusPoints

    private val _bonusProgress = MutableStateFlow(0)
    val bonusProgress: StateFlow<Int> = _bonusProgress

    private val turnScores = mutableListOf<TurnScoreDetail>()

    init {
        resetTurn()
    }

    fun initializeScoreStrings() {
        if (_minorScoreOptions.value.isEmpty() && _majorScoreOptions.value.isEmpty()) {
            val minorStrings = listOf("Ones", "Twos", "Threes", "Fours", "Fives", "Sixes")
            val majorStrings = listOf(
                "Three of a Kind", "Four of a Kind", "Full House",
                "Minor Straight", "Major Straight", "Yahtzee", "Chance"
            )
            setScoreOptionStrings(minorStrings, majorStrings)
        }
    }

    fun setScoreOptionStrings(minorStrings: List<String>, majorStrings: List<String>) {
        _minorScoreOptions.value =
            minorStrings.map { ScoreOption(it, calculateScore(_diceValues.value, it)) }
        _majorScoreOptions.value =
            majorStrings.map { ScoreOption(it, calculateScore(_diceValues.value, it)) }
    }

    fun rollDice() {
        if (_rollsRemaining.value > 0 && !_isGameOver.value) {
            val newValues = _diceValues.value.mapIndexed { index, value ->
                if (_heldDice.value[index]) value else (1..6).random()
            }
            _diceValues.value = newValues
            _rollsRemaining.update { it - 1 }
            _canRoll.value = _rollsRemaining.value > 0
            recalculateScoreOptions(newValues)
        }
    }

    private fun recalculateScoreOptions(diceValues: List<Int>) {
        _minorScoreOptions.update { minorOptions ->
            minorOptions.map { it.copy(points = calculateScore(diceValues, it.name)) }
        }
        _majorScoreOptions.update { majorOptions ->
            majorOptions.map { it.copy(points = calculateScore(diceValues, it.name)) }
        }
    }

    fun toggleHold(index: Int) {
        if (!_isGameOver.value) {
            _heldDice.update { holds ->
                holds.mapIndexed { i, isHeld -> if (i == index) !isHeld else isHeld }
            }
        }
    }

    fun selectScoreOption(option: ScoreOption) {
        if (_rollsRemaining.value < maxRollsPerTurn && !option.confirmed && !_isGameOver.value) {
            _selectedOption.value = option
        }
    }

    fun confirmSelection() {
        _selectedOption.value?.let { selectedOption ->
            // Aggiorna il punteggio totale
            _totalScore.update { it + selectedOption.points }

            // Salva i dettagli del turno
            val turnDetail = TurnScoreDetail(
                turn = _turnCount.value + 1,
                scoreOption = selectedOption.name,
                points = selectedOption.points
            )
            turnScores.add(turnDetail)

            // Conferma l'opzione
            updateConfirmedOptions(selectedOption)

            // Aggiorna il progresso bonus
            val minorOption = _minorScoreOptions.value.find { it.name == selectedOption.name }
            if (minorOption != null) {
                _bonusProgress.update { it + selectedOption.points }
            }

            // Assegna il bonus solo una volta
            if (!isBonusAwarded && _bonusProgress.value >= bonusThreshold) {
                isBonusAwarded = true
                _totalScore.update { it + bonusScore }
            }

            // Controlla fine gioco o resetta il turno
            if (_turnCount.value + 1 >= maxTurns) {
                endGame()
            } else {
                resetTurn()
            }
        }
    }

    private fun resetTurn() {
        if (!_isGameOver.value) {
            _selectedOption.value = null
            _rollsRemaining.value = maxRollsPerTurn
            _heldDice.value = List(5) { false }
            _canRoll.value = true
            _turnCount.update { it + 1 }
            _diceValues.value = List(5) { 0 }
        }
    }

    private fun endGame() {
        _isGameOver.value = true
        _canRoll.value = false

        val game = Game(
            gameId = UUID.randomUUID().toString(),
            date = LocalDateTime.now(),
            finalScore = _totalScore.value,
            scores = turnScores.toList(),
            bonusAchieved = isBonusAwarded
        )
        Log.d("SinglePlayerPageViewModel", "Game ended: $game")
        saveGameToHistory(game)
    }

    private fun saveGameToHistory(game: Game) {
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()
        val sharedPreferences = context.getSharedPreferences("game_history", Context.MODE_PRIVATE)
        val savedGamesJson = sharedPreferences.getStringSet("games", emptySet())

        val currentGames = savedGamesJson?.mapNotNull { gameJson ->
            try {
                gson.fromJson(gameJson, Game::class.java)
            } catch (e: Exception) {
                Log.e("saveGameToHistory", "Failed to parse game: $gameJson", e)
                null
            }
        }?.toMutableList() ?: mutableListOf()

        currentGames.add(0, game)

        val updatedGamesJson = currentGames.take(10).map { gson.toJson(it) }.toSet()
        sharedPreferences.edit().putStringSet("games", updatedGamesJson).apply()

        // Log dei dati salvati
        Log.d("saveGameToHistory", "Game history saved successfully. Updated Games: $updatedGamesJson")
    }



    private fun updateConfirmedOptions(selectedOption: ScoreOption) {
        _minorScoreOptions.update { list ->
            list.map {
                if (it == selectedOption) it.copy(confirmed = true)
                else it
            }
        }
        _majorScoreOptions.update { list ->
            list.map {
                if (it == selectedOption) it.copy(confirmed = true)
                else it
            }
        }
    }

    private fun calculateScore(dice: List<Int>, category: String): Int {
        return when (category) {
            "Ones" -> dice.count { it == 1 }
            "Twos" -> dice.count { it == 2 } * 2
            "Threes" -> dice.count { it == 3 } * 3
            "Fours" -> dice.count { it == 4 } * 4
            "Fives" -> dice.count { it == 5 } * 5
            "Sixes" -> dice.count { it == 6 } * 6
            "Three of a Kind" -> if (dice.groupingBy { it }.eachCount().values.any { it >= 3 }) dice.sum() else 0
            "Four of a Kind" -> if (dice.groupingBy { it }.eachCount().values.any { it >= 4 }) dice.sum() else 0
            "Full House" -> if (dice.groupingBy { it }.eachCount().values.toSet() == setOf(2, 3)) 25 else 0
            "Minor Straight" -> if (dice.toSet().containsAll(listOf(1, 2, 3, 4)) ||
                dice.toSet().containsAll(listOf(2, 3, 4, 5)) ||
                dice.toSet().containsAll(listOf(3, 4, 5, 6))) 30 else 0
            "Major Straight" -> if (dice.sorted() == listOf(1, 2, 3, 4, 5) ||
                dice.sorted() == listOf(2, 3, 4, 5, 6)) 40 else 0
            "Yahtzee" -> if (dice.distinct().size == 1) 50 else 0
            "Chance" -> dice.sum()
            else -> 0
        }
    }

    companion object {
        private const val HISTORY_KEY = "game_history"
    }
}
