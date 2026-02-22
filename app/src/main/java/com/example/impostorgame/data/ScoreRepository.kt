package com.example.impostorgame.data

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Manages persisting and retrieving player scores using SharedPreferences.
 */
class ScoreRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    // We keep a reactive flow of the scores to update the UI immediately
    private val _scores = MutableStateFlow<Map<String, Int>>(loadScores())
    val scores: StateFlow<Map<String, Int>> = _scores.asStateFlow()

    private fun loadScores(): Map<String, Int> {
        val allEntries = prefs.all
        val scoreMap = mutableMapOf<String, Int>()
        for ((key, value) in allEntries) {
            if (value is Int) {
                scoreMap[key] = value
            }
        }
        return scoreMap.toList().sortedByDescending { it.second }.toMap()
    }

    /**
     * Adds points to a player's score.
     */
    fun addScore(playerName: String, points: Int) {
        val currentScore = prefs.getInt(playerName, 0)
        val newScore = currentScore + points
        prefs.edit().putInt(playerName, newScore).apply()
        
        // Refresh flow
        _scores.value = loadScores()
    }

    /**
     * Resets all scores back to zero.
     */
    fun clearAllScores() {
        prefs.edit().clear().apply()
        _scores.value = emptyMap()
    }

    companion object {
        private const val PREFS_NAME = "impostor_game_scores"
        
        // Singleton instance
        @Volatile
        private var INSTANCE: ScoreRepository? = null

        fun getInstance(context: Context): ScoreRepository {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: ScoreRepository(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
