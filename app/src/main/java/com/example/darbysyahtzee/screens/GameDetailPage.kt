package com.example.darbysyahtzee.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.composables.Game

import com.example.darbysyahtzee.ui.theme.CreamBackground

@Composable
fun GameDetailPage(game: Game, navController: NavController) {
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
            Text(
                text = stringResource(R.string.game_details),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.final_score, game.finalScore),
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Header of the table
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.secondary.copy(alpha = 0.1f))
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.turn), fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                Text(text = stringResource(R.string.score_option), fontWeight = FontWeight.Bold, modifier = Modifier.weight(2f))
                Text(text = stringResource(R.string.points), fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
            }

            Divider(color = MaterialTheme.colorScheme.primary, thickness = 1.dp)

            // Table body
            LazyColumn(
                modifier = Modifier.weight(1f), // Makes space for the footer
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(game.scores) { detail ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 10.dp)
                                .background(MaterialTheme.colorScheme.surface),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "${detail.turn - 1}",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${detail.scoreOption}",
                                modifier = Modifier
                                    .weight(2f)
                                    .padding(horizontal = 8.dp),
                                fontSize = 14.sp
                            )
                            Text(
                                text = "${detail.points}",
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(horizontal = 8.dp),
                                fontSize = 14.sp
                            )
                        }
                        Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    }
                }
            }

            // Bonus Section
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = if (game.bonusAchieved) {
                    "Bonus Achieved! (+35 Points)"
                } else {
                    "Bonus Not Achieved"
                },
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = if (game.bonusAchieved) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )


            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = { navController.popBackStack() }) {
                Text(text = "Back to History")
            }
        }
    }
}
