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
import androidx.navigation.NavController
import com.example.darbysyahtzee.R
import com.example.darbysyahtzee.composables.GameRulesGrid
import com.example.darbysyahtzee.ui.theme.*

@Composable
fun SinglePlayerPage(navController: NavController) {
    // UI Layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CreamBackground) // Using the same background as HomePage
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Scoreboard Section
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.actual_score),
                    fontFamily = titleFont(),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF000000),
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Game grid / Rules Area
                GameRulesGrid()
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Roll Dice Button
           // DiceRollerButton()
        }
    }
}
