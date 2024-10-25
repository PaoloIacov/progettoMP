package com.example.darbysyahtzee

import HomePage
import SinglePlayerPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.darbysyahtzee.ui.theme.DArbysYahtzeeTheme
import com.example.darbysyahtzee.composables.NavBar
import com.example.darbysyahtzee.navigation.NavigationItem
import com.example.darbysyahtzee.screens.MultiPlayerPage
import com.example.darbysyahtzee.viewModels.HomePageViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            LocalContext.current
            DArbysYahtzeeTheme {

                // Scaffold gestisce la struttura delle pagine
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        NavBar(navController = navController) //Navigation Bar di sotto
                    }
                ) { innerPadding ->
                   //NavHost gestisce la navigazione
                    NavHost(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        startDestination = NavigationItem.Play.route
                    ) {
                        composable(NavigationItem.Play.route) {
                            HomePage(
                                navController = navController,
                                viewModel = HomePageViewModel()
                            )
                        }

                        composable(NavigationItem.Account.route) {
                            Text("Account")
                        }

                        composable("Singleplayer") {
                            SinglePlayerPage(
                                navController = navController
                            )
                        }

                        composable("Multiplayer") {
                            MultiPlayerPage(
                                navController = navController
                            )
                        }

                        composable("History") {
                            Text("History")
                        }

                    }
                }
            }
        }
    }
}

