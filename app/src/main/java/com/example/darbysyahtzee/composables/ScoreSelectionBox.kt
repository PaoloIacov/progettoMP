package com.example.darbysyahtzee.composables

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Composable
fun ScoreSelectionBox(
    scoreOption: ScoreOption,
    onSelect: (ScoreOption) -> Unit
) {
    val confirmed by scoreOption.confirmedState.collectAsState()

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(60.dp)

            //Disable the click if the option is already confirmed
            .clickable(enabled = !confirmed) {
                if (!confirmed) {
                    onSelect(scoreOption)
                    Log.d("ScoreSelectionBox", "ScoreOption selected: ${scoreOption}")
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (scoreOption.selected) Color.Blue else Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = scoreOption.name,
                fontSize = 14.sp,
                maxLines = 1,
            )
            Text(
                text = "${scoreOption.points} points",
                fontSize = 12.sp,
                maxLines = 1,
            )
        }
    }
}

data class ScoreOption(
    val name: String,
    val points: Int,
    var selected: Boolean = false,
    var confirmed: Boolean = false
) {
    var _confirmedState = MutableStateFlow(confirmed)
    val confirmedState: StateFlow<Boolean> = _confirmedState.asStateFlow()
}
