import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class HistoryPageViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistoryPageViewModel::class.java)) {
            return HistoryPageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
