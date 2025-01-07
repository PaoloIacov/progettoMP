import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import com.example.darbysyahtzee.composables.Game
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ProfilePageViewModel(context: Context) : ViewModel() {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("game_history", Context.MODE_PRIVATE)

    private val gson: Gson = GsonBuilder()
        .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
        .create()

    private val _nickname = MutableStateFlow("Player")
    val nickname: StateFlow<String> = _nickname

    private val _gamesPlayed = MutableStateFlow(0)
    val gamesPlayed: StateFlow<Int> = _gamesPlayed

    private val _highScore = MutableStateFlow(0)
    val highScore: StateFlow<Int> = _highScore

    private val _averageScore = MutableStateFlow(0f)
    val averageScore: StateFlow<Float> = _averageScore

    private val _yatzyCount = MutableStateFlow(0)
    val yatzyCount: StateFlow<Int> = _yatzyCount

    private val _maxYatzyInGame = MutableStateFlow(0)
    val maxYatzyInGame: StateFlow<Int> = _maxYatzyInGame

    init {
        loadStats()
        loadNickname()
    }

    private fun loadStats() {
        val savedGamesJson = sharedPreferences.getStringSet("games", emptySet())

        val games = savedGamesJson?.mapNotNull { gameJson ->
            try {
                gson.fromJson(gameJson, Game::class.java)
            } catch (e: Exception) {
                null
            }
        } ?: emptyList()

        _gamesPlayed.value = games.size
        _highScore.value = games.maxOfOrNull { it.finalScore } ?: 0
        _averageScore.value = if (games.isNotEmpty()) games.map { it.finalScore }.average().toFloat() else 0f
        _yatzyCount.value = games.sumOf { game -> game.scores.count { it.scoreOption == "Yahtzee" && it.points > 0 } }
        _maxYatzyInGame.value = games.maxOfOrNull { game -> game.scores.count { it.scoreOption == "Yahtzee" && it.points > 0 } } ?: 0
    }

    private fun loadNickname() {
        _nickname.value = sharedPreferences.getString("nickname", "Player") ?: "Player"
    }

    fun updateNickname(newNickname: String) {
        _nickname.value = newNickname
        sharedPreferences.edit().putString("nickname", newNickname).apply()
    }
}
