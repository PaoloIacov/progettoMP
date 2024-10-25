package com.example.darbysyahtzee.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.darbysyahtzee.composables.ScoreOption
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MultiPlayerPageViewModel : ViewModel() {

    // Holds the current dice values
    private val _diceValues = MutableStateFlow(List(5) { (1..6).random() })
    val diceValues: StateFlow<List<Int>> = _diceValues

    // Holds the hold state for each dice (true if the dice is held)
    private val _diceHeld = MutableStateFlow(List(5) { false })
    val diceHeld: StateFlow<List<Boolean>> = _diceHeld

    // Counts the number of rolls the player has made (max 3)
    private val _rollCount = MutableStateFlow(0)
    val rollCount: StateFlow<Int> = _rollCount

    // Controls whether the player can roll the dice (enabled until 3 rolls)
    private val _canRoll = MutableStateFlow(true)
    val canRoll: StateFlow<Boolean> = _canRoll

    // Score options
    private val _minorScoreOptions = MutableStateFlow(emptyList<ScoreOption>())
    val minorScoreOptions: StateFlow<List<ScoreOption>> = _minorScoreOptions

    private val _majorScoreOptions = MutableStateFlow(emptyList<ScoreOption>())
    val majorScoreOptions: StateFlow<List<ScoreOption>> = _majorScoreOptions

    // Selected score option
    private val _selectedOption = MutableStateFlow<ScoreOption?>(null)
    val selectedOption: StateFlow<ScoreOption?> = _selectedOption

    // Total score
    private val _totalScore = MutableStateFlow(0)
    val totalScore: StateFlow<Int> = _totalScore


    // Roll the dice while keeping the held dice
    fun rollDice() {
        if (_rollCount.value < 3) {
            _diceValues.value = _diceValues.value.mapIndexed { index, value ->
                if (_diceHeld.value[index]) value else (1..6).random()
            }
            _rollCount.value += 1
            _canRoll.value = _rollCount.value < 3
        }
    }

    // Toggle the hold state of a dice
    fun toggleHoldDice(index: Int) {
        _diceHeld.value = _diceHeld.value.mapIndexed { i, held ->
            if (i == index) !held else held
        }
    }

    // Confirm the selected score option and add points to the total score
    fun confirmSelection() {
        _selectedOption.value?.let { selectedOption ->
            _totalScore.value += selectedOption.points
            _minorScoreOptions.value = _minorScoreOptions.value.map {
                if (it == selectedOption) it.copy(confirmed = true) else it
            }
            _majorScoreOptions.value = _majorScoreOptions.value.map {
                if (it == selectedOption) it.copy(confirmed = true) else it
            }
            _selectedOption.value = null
            resetAfterTurn()  // Reset for the next turn
        }
    }

    // Reset the game state after a player's turn
    private fun resetAfterTurn() {
        _rollCount.value = 0
        _diceHeld.value = List(5) { false }
        _canRoll.value = true
    }

    // Initialize the score options with the passed strings
    fun setScoreOptionStrings(minorStrings: List<String>, majorStrings: List<String>) {
        _minorScoreOptions.value = minorStrings.map { name ->
            ScoreOption(name, calculateScore(_diceValues.value, name))
        }
        _majorScoreOptions.value = majorStrings.map { name ->
            ScoreOption(name, calculateScore(_diceValues.value, name))
        }
    }

    // Selects a score option
    fun selectScoreOption(option: ScoreOption) {
        if (!option.confirmed) {
            _selectedOption.value = option
        }
    }

    // Rolls the dice
    fun rollDice(newValues: List<Int>) {
        _diceValues.value = newValues
        _canRoll.value = false // Disable rolling until next turn
    }

    // Score calculation
    private fun calculateScore(dice: List<Int>, category: String): Int {
        return when (category) {
            "Ones" -> dice.count { it == 1 } * 1
            "Twos" -> dice.count { it == 2 } * 2
            "Threes" -> dice.count { it == 3 } * 3
            "Fours" -> dice.count { it == 4 } * 4
            "Fives" -> dice.count { it == 5 } * 5
            "Sixes" -> dice.count { it == 6 } * 6
            "Three of a Kind" -> if (dice.groupBy { it }.values.any { it.size >= 3 }) dice.sum() else 0
            "Four of a Kind" -> if (dice.groupBy { it }.values.any { it.size >= 4 }) dice.sum() else 0
            "Full House" -> {
                val groups = dice.groupBy { it }.values.map { it.size }
                if (groups.contains(3) && groups.contains(2)) 25 else 0
            }
            "Minor Straight" -> if (dice.sorted().windowed(4).any {
                    it == listOf(1, 2, 3, 4) ||
                            it == listOf(2, 3, 4, 5) ||
                            it == listOf(3, 4, 5, 6)
                }) 30 else 0
            "Major Straight" -> if (dice.sorted().windowed(5).any {
                    it == listOf(1, 2,
                        3, 4, 5) ||
                            it == listOf(2, 3, 4, 5, 6)
                }) 40 else 0
            "Yahtzee" -> if (dice.distinct().size == 1) 50 else 0
            "Chance" -> dice.sum()
            else -> 0
        }
    }
}
