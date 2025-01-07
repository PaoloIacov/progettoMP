package com.example.darbysyahtzee.viewModels

import ProfilePageViewModel
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ProfilePageViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilePageViewModel::class.java)) {
            return ProfilePageViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
