package com.example.darbysyahtzee.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.composables.DiceRollerButton
import com.example.darbysyahtzee.composables.ScoreSelectionBox
import com.example.darbysyahtzee.composables.ScoreStrings
import com.example.darbysyahtzee.ui.theme.CreamBackground
import com.example.darbysyahtzee.viewModels.MultiPlayerPageViewModel


@Composable
fun MultiPlayerPage(navController: NavController) {
    val viewModel: MultiPlayerPageViewModel = viewModel()

    // Collect necessary state from the ViewModel
    val canRoll by viewModel.canRoll.collectAsState()
    val diceValues by viewModel.diceValues.collectAsState()
    val diceHeld by viewModel.diceHeld.collectAsState()
    val rollCount by viewModel.rollCount.collectAsState()

    val minorScoreOptions by viewModel.minorScoreOptions.collectAsState()
    val majorScoreOptions by viewModel.majorScoreOptions.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()

    // Initialize score strings
    ScoreStrings(viewModel)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Scoreboard section
            Text(
                text = stringResource(id = R.string.actual_score, viewModel.totalScore.value),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dice roller section with dice holding
            DiceRollerButton(
                diceValues = diceValues,
                diceHeld = diceHeld,
                onDiceRolled = { viewModel.rollDice() }, // Handles rolling logic in ViewModel
                onToggleHold = { index -> viewModel.toggleHoldDice(index) }, // Handles dice hold/unhold
                rollCount = rollCount,
                canRoll = canRoll
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Display score options (divided into minor and major score options)
            Row(modifier = Modifier.fillMaxWidth()) {
                // Minor score options on the left side
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp)
                ) {
                    minorScoreOptions.forEach { option ->
                        ScoreSelectionBox(
                            scoreOption = option,
                            onSelect = { viewModel.selectScoreOption(it) }
                        )
                    }
                }

                // Major score options on the right side
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                ) {
                    majorScoreOptions.forEach { option ->
                        ScoreSelectionBox(
                            scoreOption = option,
                            onSelect = { viewModel.selectScoreOption(it) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Confirm selection button
            Button(
                onClick = {
                    viewModel.confirmSelection()
                    Log.d("MultiPlayerPage", "ScoreOption confirmed: $selectedOption")
                },
                enabled = selectedOption != null // Button enabled only if an option is selected
            ) {
                Text(stringResource(R.string.confirm_selection))
            }
        }
    }
}
