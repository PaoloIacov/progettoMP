import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.composables.GameOptionCard
import com.example.darbysyahtzee.ui.theme.*
import com.example.darbysyahtzee.viewModels.HomePageEvent
import com.example.darbysyahtzee.viewModels.HomePageViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun HomePage(navController: NavController, viewModel: HomePageViewModel = viewModel()) {
    // Observe the state from the ViewModel
    val event by viewModel.events.collectAsState()

    LaunchedEffect(event) {
        event?.let {
            when (it) {
                HomePageEvent.NavigateToSinglePlayer -> {
                    navController.navigate("singleplayer")
                }
                HomePageEvent.NavigateToMultiplayer -> {
                    navController.navigate("multiplayer")
                }
                HomePageEvent.NavigateToHistory -> {
                    navController.navigate("history")
                }
            }
            // Clear the event after handling
            viewModel.clearEvents()
        }
    }

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
            // Title: "D'Arby's Yahtzee"
            Text(
                text = "D'Arby's \n Yahtzee",
                fontFamily = titleFont(),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 32.sp,
                color = Color(0xFF37474F),
                modifier = Modifier.padding(bottom = 0.dp)
            )
            Spacer(modifier = Modifier.height(48.dp))

            // Card: "Gioca in solitario"
            GameOptionCard(
                title = "Gioca in solitario",
                description = "Sfida te stesso \nin una partita solitaria",
                backgroundColor = RedCard,
                imageResource = R.drawable.play_single_player,
                onClick = { viewModel.onSinglePlayerClicked() }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Card: "Gioca multigiocatore"
            GameOptionCard(
                title = "Gioca multigiocatore",
                description = "Sfida altri giocatori online",
                backgroundColor = BlueCard,
                imageResource = R.drawable.play_multiplayer,
                onClick = { viewModel.onMultiplayerClicked() }
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Card: "Vedi storico delle tue partite"
            GameOptionCard(
                title = "Vedi storico \ndelle tue partite",
                description = "Controlla le tue partite passate",
                backgroundColor = GreenCard,
                imageResource = R.drawable.check_previous_games,
                onClick = { viewModel.onHistoryClicked() }
            )
        }
    }
}