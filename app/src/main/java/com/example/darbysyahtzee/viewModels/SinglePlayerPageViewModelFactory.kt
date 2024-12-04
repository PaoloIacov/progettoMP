package com.example.darbysyahtzee.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SinglePlayerPageViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SinglePlayerPageViewModel::class.java)) {
            return SinglePlayerPageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
