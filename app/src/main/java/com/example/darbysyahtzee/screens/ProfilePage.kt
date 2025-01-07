package com.example.darbysyahtzee.screens

import ProfilePageViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.ui.theme.CreamBackground

@Composable
fun ProfilePage(navController: NavController, viewModel: ProfilePageViewModel) {
    // Osserva i dati del ViewModel
    val nickname by viewModel.nickname.collectAsState()
    val gamesPlayed by viewModel.gamesPlayed.collectAsState()
    val highScore by viewModel.highScore.collectAsState()
    val averageScore by viewModel.averageScore.collectAsState()
    val yatzyCount by viewModel.yatzyCount.collectAsState()
    val maxYatzyInGame by viewModel.maxYatzyInGame.collectAsState()

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
            // Titolo della pagina
            Text(
                text = stringResource(R.string.profile),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Mostra il nickname e il pulsante per modificarlo
            Text(
                text = nickname,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { /* Logica per modificare il nickname */ }) {
                Text(text = stringResource(R.string.edit_nickname))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sezione statistiche personali
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), thickness = 1.dp)

            Text(
                text = stringResource(R.string.statistics),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileStatisticRow(label = stringResource(R.string.games_played), value = gamesPlayed.toString())
            ProfileStatisticRow(label = stringResource(R.string.high_score), value = highScore.toString())
            ProfileStatisticRow(label = stringResource(R.string.average_score), value = String.format("%.2f", averageScore))
            ProfileStatisticRow(label = stringResource(R.string.yahtzee_count), value = yatzyCount.toString())
            ProfileStatisticRow(label = stringResource(R.string.max_yahtzee_in_a_game), value = maxYatzyInGame.toString())

            Spacer(modifier = Modifier.height(16.dp))

            // Sezione statistiche multiplayer (se applicabile)
            Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f), thickness = 1.dp)

            Text(
                text = stringResource(R.string.multiplayer_stats),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            ProfileStatisticRow(label = stringResource(R.string.wins), value = "0") // Placeholder
            ProfileStatisticRow(label = stringResource(R.string.losses), value = "0") // Placeholder
            ProfileStatisticRow(label = stringResource(R.string.win_streak), value = "0") // Placeholder

            Spacer(modifier = Modifier.height(16.dp))

            // Pulsante per tornare indietro
            Button(onClick = { navController.popBackStack() }) {
                Text(text = stringResource(R.string.back))
            }
        }
    }
}

@Composable
fun ProfileStatisticRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
