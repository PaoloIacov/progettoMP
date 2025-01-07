package com.example.darbysyahtzee.screens

import HistoryPageViewModel
import HistoryPageViewModelFactory
import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.composables.HistoryCard


@Composable
fun HistoryPage(navController: NavController, context: Context) {
    val viewModel: HistoryPageViewModel = viewModel(factory = HistoryPageViewModelFactory(context))
    val gameHistory by viewModel.gameHistory.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.refreshHistory()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.game_history),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (gameHistory.isEmpty()) {
            Text(
                text = stringResource(R.string.no_games_played_yet),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(gameHistory.size) { index ->
                    val game = gameHistory[index]
                    HistoryCard(game = game) {
                        navController.navigate("GameDetail/${game.gameId}")
                    }
                }
            }
        }
    }
}
