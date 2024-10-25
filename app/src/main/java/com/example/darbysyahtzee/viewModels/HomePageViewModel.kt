package com.example.darbysyahtzee.viewModels


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//Possibili eventi
sealed class HomePageEvent {
    object NavigateToSinglePlayer : HomePageEvent()
    object NavigateToMultiplayer : HomePageEvent()
    object NavigateToHistory : HomePageEvent()
}

class HomePageViewModel : ViewModel() {

    private val _events = MutableStateFlow<HomePageEvent?>(null)
    val events: StateFlow<HomePageEvent?> = _events

    //Triggers
    fun onSinglePlayerClicked() {
        viewModelScope.launch {
            _events.emit(HomePageEvent.NavigateToSinglePlayer)
        }
    }

    fun onMultiplayerClicked() {
        viewModelScope.launch {
            _events.emit(HomePageEvent.NavigateToMultiplayer)
        }
    }

    fun onHistoryClicked() {
        viewModelScope.launch {
            _events.emit(HomePageEvent.NavigateToHistory)
        }
    }

    fun clearEvents() {
        viewModelScope.launch {
            _events.emit(null)
        }
    }
}

