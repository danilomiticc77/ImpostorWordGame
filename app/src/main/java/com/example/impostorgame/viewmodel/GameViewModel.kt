package com.example.impostorgame.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.impostorgame.data.CategoryRepository
import com.example.impostorgame.data.CustomCategoryManager
import com.example.impostorgame.data.ScoreRepository
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Category
import com.example.impostorgame.model.GameState
import com.example.impostorgame.model.Language
import com.example.impostorgame.model.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * Central ViewModel managing all game state and logic.
 * Manages the core game loop, timers, and player distribution.
 */
class GameViewModel(application: Application) : AndroidViewModel(application) {

    // Main game state driving the UI
    private val _gameState = MutableStateFlow<GameState>(GameState.Setup())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()

    // Global language setting, persisted internally here 
    private val _language = MutableStateFlow(Language.EN)
    val language: StateFlow<Language> = _language.asStateFlow()

    // Track rounds for troll mode probability
    private var roundCount = 0
    private var timerJob: Job? = null

    // All available categories (base + custom)
    private val _allCategories = MutableStateFlow<List<Category>>(emptyList())
    val allCategories = _allCategories.asStateFlow()

    // Leaderboard scores
    val scores = ScoreRepository.getInstance(application).scores

    // =============== SETUP ACTIONS ===============
    init {
        loadCategories()
        _gameState.value = GameState.Setup(language = _language.value)
    }

    fun loadCategories() {
        val base = CategoryRepository.allCategories
        val custom = CustomCategoryManager.getCustomCategories(getApplication())
        _allCategories.value = base + custom
    }

    fun setLanguage(lang: Language) {
        _language.value = lang
        updateSetup { it.copy(language = lang) }
    }

    fun setPlayerCount(count: Int) {
        val clamped = count.coerceIn(3, 20)
        updateSetup { state ->
            // Ensure impostor count stays valid when player count changes
            val maxImpostors = (clamped - 1).coerceAtLeast(1)
            // Resize names list to match new player count
            val newNames = List(clamped) { index ->
                state.playerNames.getOrElse(index) { "" }
            }
            state.copy(
                playerCount = clamped,
                impostorCount = state.impostorCount.coerceIn(1, maxImpostors),
                playerNames = newNames
            )
        }
    }

    fun setPlayerName(index: Int, name: String) {
        updateSetup { state ->
            val names = state.playerNames.toMutableList()
            if (index in names.indices) {
                names[index] = name
            }
            state.copy(playerNames = names)
        }
    }

    fun setImpostorCount(count: Int) {
        updateSetup { state ->
            val maxImpostors = (state.playerCount - 1).coerceAtLeast(1)
            state.copy(impostorCount = count.coerceIn(1, maxImpostors))
        }
    }

    fun toggleCategory(categoryId: String) {
        updateSetup { state ->
            val newSelection = state.selectedCategoryIds.toMutableSet()
            if (newSelection.contains(categoryId)) {
                newSelection.remove(categoryId)
            } else {
                newSelection.add(categoryId)
            }
            state.copy(selectedCategoryIds = newSelection)
        }
    }

    fun selectAllCategories() {
        updateSetup { state ->
            state.copy(selectedCategoryIds = _allCategories.value.map { it.id }.toSet())
        }
    }

    fun deselectAllCategories() {
        updateSetup { state ->
            state.copy(selectedCategoryIds = emptySet())
        }
    }

    fun setTrollMode(enabled: Boolean) {
        updateSetup { it.copy(trollModeEnabled = enabled) }
    }

    fun setSpyMode(enabled: Boolean) {
        updateSetup { it.copy(spyModeEnabled = enabled) }
    }

    fun setSpyHintLevel(level: Int) {
        updateSetup { it.copy(spyHintLevel = level) }
    }

    fun setPunishmentsEnabled(enabled: Boolean) {
        updateSetup { it.copy(punishmentsEnabled = enabled) }
    }

    fun setCategoryHint(enabled: Boolean) {
        updateSetup { it.copy(categoryHintEnabled = enabled) }
    }

    fun setTimerEnabled(enabled: Boolean) {
        updateSetup { it.copy(timerEnabled = enabled) }
    }

    fun setTimerSeconds(seconds: Int) {
        updateSetup { it.copy(timerSeconds = seconds.coerceIn(30, 600)) }
    }

    // =============== GAME START ===============

    /**
     * Starts the game: picks a random word, assigns impostors, handles troll mode.
     * Returns true if game started successfully, false if validation fails.
     */
    fun startGame(): Boolean {
        val setup = _gameState.value as? GameState.Setup ?: return false
        val lang = _language.value

        // Validation
        if (setup.selectedCategoryIds.isEmpty()) return false
        if (setup.playerCount < 3) return false

        roundCount++

        // Filter valid words from active categories
        val selectedCategoryList = allCategories.value.filter { setup.selectedCategoryIds.contains(it.id) }
        if (selectedCategoryList.isEmpty()) return false

        val words = selectedCategoryList.flatMap { it.getWords(lang) }
        if (words.isEmpty()) return false

        val chosenCategory = selectedCategoryList.random()
        val chosenWord = words.random()
        val categoryName = chosenCategory.getName(lang)

        // Determine if this is a troll round (~10% chance when enabled)
        val isTrollRound = setup.trollModeEnabled && (roundCount % 10 == 0 || (Math.random() < 0.1))

        // Assign players and impostors
        val playerIndices = (1..setup.playerCount).toList()
        val players: List<Player>
        val trollHints: List<String>

        // Resolve nicknames: use entered name or fallback to "Player X"
        val names = setup.playerNames

        if (isTrollRound) {
            // TROLL MODE: Everyone is an impostor! Give each a random hint
            players = playerIndices.map {
                val displayName = names.getOrElse(it - 1) { "" }.ifBlank { "${Strings.playerLabel(lang)} $it" }
                Player(number = it, name = displayName, isImpostor = true)
            }
            // Generate random hints from all words across all selected categories
            val allWords = selectedCategoryList.flatMap { it.getWords(lang) }
            trollHints = playerIndices.map { allWords.random().hint }
        } else {
            // Normal mode: randomly pick impostors
            val shuffled = playerIndices.shuffled()
            val impostorIndices = shuffled.take(setup.impostorCount).toSet()
            // Pick one spy from remaining non-impostors (if enabled)
            val spyIndex = if (setup.spyModeEnabled) {
                shuffled.drop(setup.impostorCount).firstOrNull()
            } else null

            players = playerIndices.map { idx ->
                val displayName = names.getOrElse(idx - 1) { "" }.ifBlank { "${Strings.playerLabel(lang)} $idx" }
                Player(
                    number = idx,
                    name = displayName,
                    isImpostor = idx in impostorIndices,
                    isSpy = idx == spyIndex
                )
            }
            trollHints = emptyList()
        }

        _gameState.value = GameState.RoleReveal(
            players = players,
            currentPlayerIndex = 0,
            secretWord = chosenWord,
            categoryName = categoryName,
            isTrollRound = isTrollRound,
            trollHints = trollHints,
            categoryHintEnabled = setup.categoryHintEnabled,
            isRevealing = false,
            hasViewed = false
        )
        return true
    }

    // =============== ROLE REVEAL ACTIONS ===============

    /** Called when finger presses down — show card */
    fun startReveal() {
        val state = _gameState.value as? GameState.RoleReveal ?: return
        _gameState.value = state.copy(isRevealing = true, hasViewed = true)
    }

    /** Called when finger releases — hide card */
    fun stopReveal() {
        val state = _gameState.value as? GameState.RoleReveal ?: return
        _gameState.value = state.copy(isRevealing = false)
    }

    fun nextPlayer() {
        val state = _gameState.value as? GameState.RoleReveal ?: return

        // First: hide content and reset viewed flag to prevent glitch
        _gameState.value = state.copy(isRevealing = false, hasViewed = false)

        val nextIndex = state.currentPlayerIndex + 1

        if (nextIndex >= state.players.size) {
            // All players have viewed
            val setup = getSetupState()
            // Always move to GameReady, pass timerEnabled so UI knows whether to show it
            _gameState.value = GameState.GameReady(
                players = state.players,
                secretWord = state.secretWord,
                categoryName = state.categoryName,
                isTrollRound = state.isTrollRound,
                timerEnabled = setup?.timerEnabled ?: true,
                timerSeconds = setup?.timerSeconds ?: 120,
                timerRunning = false
            )
        } else {
            _gameState.value = state.copy(
                currentPlayerIndex = nextIndex,
                isRevealing = false,
                hasViewed = false
            )
        }
    }

    /**
     * Returns the content to display for the current player during reveal.
     * For normal players: the word.
     * For impostors: "Impostor!" + hint.
     * For troll mode: "Impostor!" + random hint.
     */
    fun getRevealContent(): RevealContent? {
        val state = _gameState.value as? GameState.RoleReveal ?: return null
        val player = state.players[state.currentPlayerIndex]
        val lang = _language.value

        return if (player.isImpostor) {
            val hint = if (state.isTrollRound && state.trollHints.isNotEmpty()) {
                state.trollHints[state.currentPlayerIndex % state.trollHints.size]
            } else {
                state.secretWord.hint
            }
            RevealContent(
                isImpostor = true,
                title = Strings.youAreImpostor(lang),
                mainText = "${Strings.hintLabel(lang)} $hint",
                categoryText = if (state.categoryHintEnabled) {
                    "${Strings.categoryLabel(lang)} ${state.categoryName}"
                } else null
            )
        } else if (player.isSpy) {
            val impostorNames = state.players.filter { it.isImpostor }.joinToString(", ") { it.name }
            val setup = getSetupState()
            val spyLevel = setup?.spyHintLevel ?: 0

            val spyMainText = when (spyLevel) {
                0 -> "${Strings.spySeesImpostor(lang, impostorNames)}\n\n${Strings.theWordIs(lang)} ${state.secretWord.word}"
                1 -> "${Strings.spySeesImpostor(lang, impostorNames)}\n\n${Strings.categoryLabel(lang)} ${state.categoryName}"
                2 -> "${Strings.spySeesImpostor(lang, impostorNames)}\n\n${Strings.hintLabel(lang)} ${state.secretWord.hint}"
                3 -> "${Strings.spySeesImpostor(lang, impostorNames)}\n\n${Strings.hintLabel(lang)} ${state.secretWord.hint}\n${Strings.categoryLabel(lang)} ${state.categoryName}"
                else -> Strings.spySeesImpostor(lang, impostorNames)
            }

            RevealContent(
                isImpostor = true,
                title = Strings.youAreSpy(lang),
                mainText = spyMainText,
                categoryText = null
            )
        } else {
            RevealContent(
                isImpostor = false,
                title = Strings.theWordIs(lang),
                mainText = state.secretWord.word,
                categoryText = null
            )
        }
    }

    // =============== DISCUSSION / TIMER ===============

    fun startTimer() {
        val state = _gameState.value as? GameState.GameReady ?: return
        _gameState.value = state.copy(timerRunning = true)

        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            var remaining = state.timerSeconds
            while (remaining > 0) {
                delay(1000)
                remaining--
                val current = _gameState.value as? GameState.GameReady ?: break
                if (!current.timerRunning) break
                _gameState.value = current.copy(timerSeconds = remaining)
            }
            // Timer finished — stay on the screen so players manually click "Reveal Impostor"
        }
    }

    private fun moveToSummary() {
        val state = _gameState.value
        when (state) {
            is GameState.GameReady -> {
                _gameState.value = GameState.Summary(
                    players = state.players,
                    secretWord = state.secretWord,
                    categoryName = state.categoryName,
                    isTrollRound = state.isTrollRound
                )
            }
            else -> {}
        }
    }

    // =============== IMPOSTOR REVEAL -> SUMMARY ===============

    /** Transition from GameReady to Summary directly when "Reveal Impostor" is confirmed */
    fun revealImpostor() {
        timerJob?.cancel()
        val state = _gameState.value as? GameState.GameReady ?: return
        val setup = getSetupState()
        _gameState.value = GameState.Summary(
            players = state.players,
            secretWord = state.secretWord,
            categoryName = state.categoryName,
            isTrollRound = state.isTrollRound,
            punishmentsEnabled = setup?.punishmentsEnabled ?: false,
            scoresAwarded = false
        )
    }

    // =============== SCORING ===============
    fun awardScores(crewmatesWon: Boolean) {
        val state = _gameState.value as? GameState.Summary ?: return
        if (state.scoresAwarded) return
        
        val repository = ScoreRepository.getInstance(getApplication())
        state.players.forEach { player ->
            if (crewmatesWon && (!player.isImpostor && !player.isSpy)) {
                repository.addScore(player.name, 1)
            } else if (!crewmatesWon && (player.isImpostor || player.isSpy)) {
                repository.addScore(player.name, 2)
            }
        }

        val lang = _language.value
        val punishment = if (state.punishmentsEnabled) {
            val losers = if (crewmatesWon) Strings.theImpostor(lang) else Strings.theCrewmates(lang)
            "${Strings.punishmentFor(lang, losers)}\n\n${Strings.getRandomPunishment(lang)}"
        } else null

        _gameState.value = state.copy(scoresAwarded = true, punishmentText = punishment)
    }

    fun clearScores() {
        ScoreRepository.getInstance(getApplication()).clearAllScores()
    }

    // =============== GAME RESET ===============

    /** Restart with the same settings */
    fun playAgain() {
        val lastSetup = getSetupState() ?: GameState.Setup()
        _gameState.value = lastSetup
    }

    /** Full reset to fresh setup */
    fun newGame() {
        roundCount = 0
        timerJob?.cancel()
        _gameState.value = GameState.Setup(language = _language.value)
    }

    fun resetToSetup() {
        timerJob?.cancel()
        val setup = getSetupState() ?: GameState.Setup(language = _language.value)
        _gameState.value = setup
    }

    // =============== HELPERS ===============

    private var lastSetup: GameState.Setup? = null

    private fun getSetupState(): GameState.Setup? = lastSetup

    private fun updateSetup(transform: (GameState.Setup) -> GameState.Setup) {
        val current = _gameState.value as? GameState.Setup ?: return
        val updated = transform(current)
        lastSetup = updated
        _gameState.value = updated
    }
}

/**
 * Data class for what to display during role reveal.
 */
data class RevealContent(
    val isImpostor: Boolean,
    val title: String,
    val mainText: String,
    val categoryText: String?
)
