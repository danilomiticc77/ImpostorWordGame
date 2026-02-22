package com.example.impostorgame.model

/**
 * Represents a single word with its hint in one language.
 * @param word The secret word that normal players see
 * @param hint A short descriptive clue that impostors see
 */
data class WordItem(
    val word: String,
    val hint: String
)
