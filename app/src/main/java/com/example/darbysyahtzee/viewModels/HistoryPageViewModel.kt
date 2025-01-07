import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.darbysyahtzee.composables.Game
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDateTime

class HistoryPageViewModel(private val context: Context) : ViewModel() {
    private val _gameHistory = MutableStateFlow<List<Game>>(emptyList())
    val gameHistory: StateFlow<List<Game>> = _gameHistory

    init {
        loadGameHistory()
    }

    private fun loadGameHistory() {
        val sharedPreferences = context.getSharedPreferences("game_history", Context.MODE_PRIVATE)
        val gson = GsonBuilder()
            .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter())
            .create()

        val savedGamesJson = sharedPreferences.getStringSet("games", emptySet())

        if (savedGamesJson.isNullOrEmpty()) {
            _gameHistory.value = emptyList()
            Log.d("HistoryPageViewModel", "No saved games found.")
            return
        }

        try {
            val games = savedGamesJson.map { gameJson ->
                gson.fromJson(gameJson, Game::class.java)
            }
            _gameHistory.value = games
            Log.d("HistoryPageViewModel", "Loaded games: $games")
        } catch (e: Exception) {
            Log.e("HistoryPageViewModel", "Failed to load games: ${e.message}")
            _gameHistory.value = emptyList()
        }
    }

    fun getGameById(gameId: String): Game? {
        val game = _gameHistory.value.find { it.gameId == gameId }
        Log.d("HistoryPageViewModel", "Game retrieved: $game")
        return game
    }

    fun refreshHistory() {
        loadGameHistory()
        Log.d("HistoryPageViewModel", "History refreshed.")
    }

    fun clearGameHistory() {
        val sharedPreferences = context.getSharedPreferences("game_history", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        Log.d("HistoryPageViewModel", "Clearing game history... $editor" )
        editor.clear()
        editor.apply()
        Log.d("HistoryPageViewModel", "Game history cleared $editor.")
    }

    companion object {
        private const val HISTORY_KEY = "game_history"
    }
}
