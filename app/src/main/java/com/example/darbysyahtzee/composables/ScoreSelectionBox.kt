package com.example.darbysyahtzee.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
    val confirmed = scoreOption.confirmedState.collectAsState().value

    // Salva il valore dei punti iniziali una volta che l'opzione è confermata
    val initialPoints = remember(confirmed) {
        if (confirmed) scoreOption.points else null
    }

    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth()
            .height(60.dp)
            .clickable(enabled = !confirmed) { // La carta è cliccabile solo se non è confermata
                onSelect(scoreOption)
            },
        colors = CardDefaults.cardColors(
            containerColor = if (confirmed) Color(0xFFD6D6D6) else Color.White // Carta leggermente scura se confermata
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween, // Distribuzione fra testo e quadrato
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = scoreOption.name,
                    fontSize = 14.sp,
                    maxLines = 1,
                )
            }

            // Quadrato a destra con il punteggio
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .padding(start = 8.dp)
                    .background(
                        color = if (confirmed) Color(0xFFBFBFBF) else Color.LightGray, // Box più scuro se confermato
                        shape = RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${initialPoints ?: scoreOption.points}", // Mostra solo i punti salvati se confermato
                    fontSize = 14.sp,
                    color = Color.Black
                )
            }
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
