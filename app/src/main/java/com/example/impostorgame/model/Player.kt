package com.example.impostorgame.model

/**
 * Represents a player in the current game round.
 * @param number Player number (1-based display index)
 * @param name Player's chosen nickname
 * @param isImpostor Whether this player is an impostor
 */
data class Player(
    val number: Int,
    val name: String,
    val isImpostor: Boolean,
    val isSpy: Boolean = false
)
