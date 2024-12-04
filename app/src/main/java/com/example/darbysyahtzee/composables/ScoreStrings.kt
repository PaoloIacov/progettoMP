package com.example.darbysyahtzee.composables

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.viewModels.SinglePlayerPageViewModel

@Composable
fun ScoreStrings(viewModel: SinglePlayerPageViewModel) {
    val minorScoreStrings = listOf(
        stringResource(R.string.scoreoption_ones),
        stringResource(R.string.scoreoption_twos),
        stringResource(R.string.scoreoption_threes),
        stringResource(R.string.scoreoption_fours),
        stringResource(R.string.scoreoption_fives),
        stringResource(R.string.scoreoption_sixes)
    )

    val majorScoreStrings = listOf(
        stringResource(R.string.scoreoption_three_of_a_kind),
        stringResource(R.string.scoreoption_full_house),
        stringResource(R.string.scoreoption_four_of_a_kind),
        stringResource(R.string.scoreoption_minor_straight),
        stringResource(R.string.scoreoption_major_straight),
        stringResource(R.string.scoreoption_yahtzee),
        stringResource(R.string.scoreoption_chance)
    )

    // Pass the strings to the ViewModel
    viewModel.setScoreOptionStrings(minorScoreStrings, majorScoreStrings)
}
