package com.example.darbysyahtzee.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.composables.DiceRollerButton
import com.example.darbysyahtzee.composables.ScoreOption
import com.example.darbysyahtzee.composables.ScoreSelectionBox
import com.example.darbysyahtzee.navigation.NavigationItem
import com.example.darbysyahtzee.ui.theme.CreamBackground
import com.example.darbysyahtzee.viewModels.SinglePlayerPageViewModel
import com.example.darbysyahtzee.viewModels.SinglePlayerPageViewModelFactory

@Composable
fun SinglePlayerPage(navController: NavController) {
    val context = LocalContext.current
    val viewModel: SinglePlayerPageViewModel = viewModel(
        factory = SinglePlayerPageViewModelFactory(context)
    )

    // State values from the ViewModel
    val diceValues by viewModel.diceValues.collectAsState()
    val heldDice by viewModel.heldDice.collectAsState()
    val canRoll by viewModel.canRoll.collectAsState()
    val rollsRemaining by viewModel.rollsRemaining.collectAsState()
    val minorScoreOptions by viewModel.minorScoreOptions.collectAsState()
    val majorScoreOptions by viewModel.majorScoreOptions.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    val totalScore by viewModel.totalScore.collectAsState()
    val turnCount by viewModel.turnCount.collectAsState()
    val isGameOver by viewModel.isGameOver.collectAsState()
    val bonusPoints by viewModel.bonusPoints.collectAsState()
    val bonusProgress by viewModel.bonusProgress.collectAsState()

    // Trigger to initialize score strings
    LaunchedEffect(Unit) {
        viewModel.initializeScoreStrings()
    }

    if (isGameOver) {
        EndScreenPage(totalScore = totalScore) {
            navController.navigate(NavigationItem.Play.route)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(CreamBackground)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Turn and roll information
                Text(
                    text = stringResource(id = R.string.current_turn, turnCount),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.rolls_remaining, rollsRemaining),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = stringResource(id = R.string.actual_score, totalScore),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Dice roller
                DiceRollerButton(
                    onDiceRolled = { viewModel.rollDice() },
                    canRoll = canRoll,
                    diceValues = diceValues,
                    diceHeld = heldDice,
                    onToggleHold = { index -> viewModel.toggleHold(index) },
                    rollCount = 3 - rollsRemaining,
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Score options and bonus
                Row(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 8.dp)
                    ) {
                        // Minor Score Options
                        minorScoreOptions.forEach { option ->
                            ScoreSelectionBox(
                                scoreOption = option,
                                onSelect = { viewModel.selectScoreOption(it) }
                            )
                        }

                        // Bonus Display
                        ScoreSelectionBox(
                            scoreOption = ScoreOption(
                                name = "Bonus $bonusProgress/63",
                                points = bonusPoints,
                                confirmed = true
                            ),
                            onSelect = {} // Non-clickable
                        )
                    }
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 8.dp)
                    ) {
                        // Major Score Options
                        majorScoreOptions.forEach { option ->
                            ScoreSelectionBox(
                                scoreOption = option,
                                onSelect = { viewModel.selectScoreOption(it) }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Confirm Button
                Button(
                    onClick = { viewModel.confirmSelection() },
                    enabled = selectedOption != null
                ) {
                    Text(text = stringResource(R.string.confirm_selection))
                }
            }
        }
    }
}
