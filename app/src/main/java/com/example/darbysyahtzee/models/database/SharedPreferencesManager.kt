package com.example.darbysyahtzee.database

import android.content.Context
import android.content.SharedPreferences
import com.example.darbysyahtzee.composables.Game
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedPreferencesManager(context: Context) {
    private val preferences: SharedPreferences =
        context.getSharedPreferences("game_history_prefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveGameHistory(history: List<Game>) {
        val json = gson.toJson(history)
        preferences.edit().putString(HISTORY_KEY, json).apply()
    }

    fun getGameHistory(): List<Game> {
        val json = preferences.getString(HISTORY_KEY, null)
        return if (json != null) {
            val type = object : TypeToken<List<Game>>() {}.type
            gson.fromJson(json, type)
        } else {
            emptyList()
        }
    }

    companion object {
        private const val HISTORY_KEY = "game_history"
    }
}
