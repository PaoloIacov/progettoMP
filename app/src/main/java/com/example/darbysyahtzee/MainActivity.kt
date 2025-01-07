package com.example.darbysyahtzee

import HistoryPageViewModel
import HistoryPageViewModelFactory
import HomePage
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.darbysyahtzee.navigation.NavigationItem
import com.example.darbysyahtzee.screens.GameDetailPage
import com.example.darbysyahtzee.screens.HistoryPage
import com.example.darbysyahtzee.screens.SinglePlayerPage
import com.example.darbysyahtzee.ui.theme.DArbysYahtzeeTheme
import com.example.darbysyahtzee.composables.NavBar
import com.example.darbysyahtzee.screens.ProfilePage
import com.example.darbysyahtzee.viewModels.HomePageViewModel
import com.example.darbysyahtzee.viewModels.ProfilePageViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val context = LocalContext.current

            DArbysYahtzeeTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = { NavBar(navController = navController) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        startDestination = NavigationItem.Play.route
                    ) {
                        // Home Page (Play)
                        composable(NavigationItem.Play.route) {
                            HomePage(
                                navController = navController,
                                viewModel = HomePageViewModel()
                            )
                        }

                        // Account Page
                        composable(NavigationItem.Account.route) {
                            ProfilePage(
                                navController = navController,
                                viewModel = viewModel(factory = ProfilePageViewModelFactory(context))
                            )
                        }

                        // Singleplayer Page
                        composable("Singleplayer") {
                            SinglePlayerPage(navController = navController)
                        }

                        // Multiplayer Page
                        composable("Multiplayer") {
                            Text(
                                text = "Multiplayer",
                                modifier = Modifier.fillMaxSize(),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }

                        // History Page
                        composable("History") {
                            HistoryPage(navController = navController, context = context)
                        }

                        // Game Detail Page
                        composable(
                            route = "GameDetail/{gameId}",
                            arguments = listOf(navArgument("gameId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val context = LocalContext.current
                            val gameId = backStackEntry.arguments?.getString("gameId") ?: return@composable
                            val historyViewModel: HistoryPageViewModel = viewModel(factory = HistoryPageViewModelFactory(context))

                            val game = historyViewModel.getGameById(gameId)

                            if (game != null) {
                                GameDetailPage(game = game, navController = navController)
                            } else {
                                Text(
                                    text = "Game not found",
                                    modifier = Modifier.fillMaxSize(),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
