package com.example.darbysyahtzee.viewModels

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.darbysyahtzee.composables.ScoreOption
import com.example.darbysyahtzee.database.Game
import com.example.darbysyahtzee.database.gameDataStore
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class SinglePlayerPageViewModel(private val context: Context) : ViewModel() {
    private val maxTurns = 13
    private val maxRollsPerTurn = 3
    private val gson = Gson()

    // Game state properties
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

    // Track scores for each turn
    private val turnScores = mutableListOf<Int>()

    init {
        resetTurn()
    }

    // Initialize score categories
    fun initializeScoreStrings() {
        if (_minorScoreOptions.value.isEmpty() && _majorScoreOptions.value.isEmpty()) {
            val minorStrings = listOf("Ones", "Twos", "Threes", "Fours", "Fives", "Sixes")
            val majorStrings = listOf("Three of a Kind", "Four of a Kind", "Full House", "Minor Straight", "Major Straight", "Yahtzee", "Chance")
            setScoreOptionStrings(minorStrings, majorStrings)
        }
    }

    // Sets score options based on category strings
    fun setScoreOptionStrings(minorStrings: List<String>, majorStrings: List<String>) {
        _minorScoreOptions.value = minorStrings.map { ScoreOption(it, calculateScore(_diceValues.value, it)) }
        _majorScoreOptions.value = majorStrings.map { ScoreOption(it, calculateScore(_diceValues.value, it)) }
    }

    // Rolls dice, updates values, and recalculates score options
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

    // Recalculates points for score options
    private fun recalculateScoreOptions(diceValues: List<Int>) {
        _minorScoreOptions.update { minorOptions ->
            minorOptions.map { it.copy(points = calculateScore(diceValues, it.name)) }
        }
        _majorScoreOptions.update { majorOptions ->
            majorOptions.map { it.copy(points = calculateScore(diceValues, it.name)) }
        }
    }

    // Toggles hold state of dice
    fun toggleHold(index: Int) {
        if (!_isGameOver.value) {
            _heldDice.update { holds ->
                holds.mapIndexed { i, isHeld -> if (i == index) !isHeld else isHeld }
            }
        }
    }

    // Selects a score option if rolls have been used
    fun selectScoreOption(option: ScoreOption) {
        if (_rollsRemaining.value < maxRollsPerTurn && !option.confirmed && !_isGameOver.value) {
            _selectedOption.value = option
        }
    }

    // Confirms the selected score option, updates score, and checks for game end
    fun confirmSelection() {
        _selectedOption.value?.let { selectedOption ->
            _totalScore.update { it + selectedOption.points }
            turnScores.add(selectedOption.points)
            updateConfirmedOptions(selectedOption)

            if (_turnCount.value + 1 >= maxTurns) {
                endGame()
            } else {
                resetTurn()
            }
        }
    }

    // Resets turn state
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

    // Ends the game, marks it complete, and saves the game data
    private fun endGame() {
        _isGameOver.value = true
        _canRoll.value = false

        val game = Game(
            gameId = UUID.randomUUID().toString(),
            date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            finalScore = _totalScore.value,
            scores = turnScores.toList()
        )

        saveGameToHistory(game)
    }

    // Saves game history to DataStore
    private fun saveGameToHistory(game: Game) {
        viewModelScope.launch {
            context.gameDataStore.edit { preferences ->
                val jsonHistory = preferences[HISTORY_KEY] ?: "[]"
                val currentHistory = gson.fromJson(jsonHistory, Array<Game>::class.java).toMutableList()
                currentHistory.add(0, game) // Add new game at the start
                preferences[HISTORY_KEY] = gson.toJson(currentHistory.take(10)) // Save only the last 10 games
            }
        }
    }

    // Marks selected option as confirmed and updates score options
    private fun updateConfirmedOptions(selectedOption: ScoreOption) {
        _minorScoreOptions.update { list ->
            list.map { if (it == selectedOption) it.copy(confirmed = true) else it }
        }
        _majorScoreOptions.update { list ->
            list.map { if (it == selectedOption) it.copy(confirmed = true) else it }
        }
    }

    // Calculates score based on dice values and scoring category
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
        private val HISTORY_KEY = stringPreferencesKey("game_history")
    }
}
