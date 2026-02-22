package com.example.impostorgame.model

/**
 * Sealed class representing all possible game states.
 * Drives the UI through the game flow.
 */
sealed class GameState {

    /** Initial setup phase — configuring players, categories, etc. */
    data class Setup(
        val playerCount: Int = 4,
        val impostorCount: Int = 1,
        val playerNames: List<String> = List(4) { "" },
        val selectedCategoryIds: Set<String> = emptySet(),
        val trollModeEnabled: Boolean = false,
        val spyModeEnabled: Boolean = false,
        val spyHintLevel: Int = 0,  // 0=word, 1=category, 2=hint, 3=hint+category
        val punishmentsEnabled: Boolean = false,
        val categoryHintEnabled: Boolean = false,
        val language: Language = Language.EN,
        val timerEnabled: Boolean = true,
        val timerSeconds: Int = 120
    ) : GameState()

    /** Role reveal phase — each player views their role one by one */
    data class RoleReveal(
        val players: List<Player>,
        val currentPlayerIndex: Int,
        val secretWord: WordItem,
        val categoryName: String,
        val isTrollRound: Boolean,
        val trollHints: List<String> = emptyList(), // Random hints for troll mode
        val categoryHintEnabled: Boolean,
        val isRevealing: Boolean = false,
        val hasViewed: Boolean = false
    ) : GameState()

    /** Discussion phase — timer counts down, players discuss */
    data class GameReady(
        val players: List<Player>,
        val secretWord: WordItem,
        val categoryName: String,
        val isTrollRound: Boolean,
        val timerEnabled: Boolean = true,
        val timerSeconds: Int,
        val timerRunning: Boolean = true
    ) : GameState()

    /** Summary phase — game results revealed */
    data class Summary(
        val players: List<Player>,
        val secretWord: WordItem,
        val categoryName: String,
        val isTrollRound: Boolean,
        val punishmentsEnabled: Boolean = false,
        val scoresAwarded: Boolean = false,
        val punishmentText: String? = null
    ) : GameState()
}
