package com.example.impostorgame.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.impostorgame.model.GameState
import com.example.impostorgame.ui.screens.*
import com.example.impostorgame.viewmodel.GameViewModel

/**
 * Navigation routes for the app.
 */
object Routes {
    const val HOME = "home"
    const val SETUP = "setup"
    const val REVEAL = "reveal"
    const val READY = "ready"
    const val SUMMARY = "summary"
    const val LEADERBOARD = "leaderboard"
    const val CUSTOM_DECKS = "custom_decks"
    const val RULES = "rules"
}

/**
 * Main navigation graph wiring screens to the ViewModel.
 */
@Composable
fun AppNavigation(
    navController: NavHostController,
    viewModel: GameViewModel
) {
    val gameState by viewModel.gameState.collectAsState()
    val language by viewModel.language.collectAsState()

    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        // HOME
        composable(Routes.HOME) {
            HomeScreen(
                language = language,
                onLanguageChange = { viewModel.setLanguage(it) },
                onStartClick = {
                    // Initialize setup with current language
                    viewModel.resetToSetup()
                    navController.navigate(Routes.SETUP)
                },
                onLeaderboardClick = {
                    navController.navigate(Routes.LEADERBOARD)
                },
                onCustomDecksClick = {
                    navController.navigate(Routes.CUSTOM_DECKS)
                },
                onRulesClick = {
                    navController.navigate(Routes.RULES)
                }
            )
        }

        // SETUP
        composable(Routes.SETUP) {
            val setupState = gameState as? GameState.Setup
                ?: GameState.Setup(language = language)

            val allCategories by viewModel.allCategories.collectAsState()

            SetupScreen(
                setupState = setupState,
                language = language,
                allCategories = allCategories,
                onPlayerCountChange = { viewModel.setPlayerCount(it) },
                onImpostorCountChange = { viewModel.setImpostorCount(it) },
                onCategoryToggle = { viewModel.toggleCategory(it) },
                onSelectAll = { viewModel.selectAllCategories() },
                onDeselectAll = { viewModel.deselectAllCategories() },
                onSpyModeChange = { viewModel.setSpyMode(it) },
                onSpyHintLevelChange = { viewModel.setSpyHintLevel(it) },
                onTrollModeChange = { viewModel.setTrollMode(it) },
                onPunishmentsEnabledChange = { viewModel.setPunishmentsEnabled(it) },
                onCategoryHintChange = { viewModel.setCategoryHint(it) },
                onTimerEnabledChange = { viewModel.setTimerEnabled(it) },
                onTimerChange = { viewModel.setTimerSeconds(it) },
                onPlayerNameChange = { index, name -> viewModel.setPlayerName(index, name) },
                onStartGame = {
                    if (viewModel.startGame()) {
                        navController.navigate(Routes.REVEAL) {
                            popUpTo(Routes.SETUP) { inclusive = true }
                        }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }

        // ROLE REVEAL
        composable(Routes.REVEAL) {
            val revealState = gameState as? GameState.RoleReveal

            if (revealState != null) {
                val currentPlayer = revealState.players[revealState.currentPlayerIndex]
                val revealContent = viewModel.getRevealContent()

                RoleRevealScreen(
                    playerName = currentPlayer.name,
                    playerNumber = currentPlayer.number,
                    totalPlayers = revealState.players.size,
                    language = language,
                    isRevealing = revealState.isRevealing,
                    hasViewed = revealState.hasViewed,
                    revealContent = revealContent,
                    onPressDown = { viewModel.startReveal() },
                    onPressUp = { viewModel.stopReveal() },
                    onNextPlayer = {
                        viewModel.nextPlayer()
                        // Check where we navigated after last player
                        val newState = viewModel.gameState.value
                        when (newState) {
                            is GameState.GameReady -> {
                                navController.navigate(Routes.READY) {
                                    popUpTo(Routes.REVEAL) { inclusive = true }
                                }
                            }
                            is GameState.Summary -> {
                                navController.navigate(Routes.SUMMARY) {
                                    popUpTo(Routes.REVEAL) { inclusive = true }
                                }
                            }
                            else -> {}
                        }
                    }
                )
            }
        }

        // DISCUSSION TIMER
        composable(Routes.READY) {
            val readyState = gameState as? GameState.GameReady

            if (readyState != null) {
                val starterPlayer = remember(readyState) {
                    readyState.players.random().name
                }
                GameReadyScreen(
                    timerEnabled = readyState.timerEnabled,
                    timerSeconds = readyState.timerSeconds,
                    timerRunning = readyState.timerRunning,
                    starterPlayerName = starterPlayer,
                    language = language,
                    onStartTimer = { viewModel.startTimer() },
                    onRevealImpostor = {
                        viewModel.revealImpostor()
                        navController.navigate(Routes.SUMMARY) {
                            popUpTo(Routes.READY) { inclusive = true }
                        }
                    }
                )
            }

            // If ViewModel already moved to Summary (e.g. timeout), navigate once
            LaunchedEffect(gameState) {
                if (gameState is GameState.Summary) {
                    navController.navigate(Routes.SUMMARY) {
                        popUpTo(Routes.READY) { inclusive = true }
                    }
                }
            }
        }

        // SUMMARY
        composable(Routes.SUMMARY) {
            val summaryState = gameState as? GameState.Summary

            if (summaryState != null) {
                SummaryScreen(
                    players = summaryState.players,
                    secretWord = summaryState.secretWord,
                    categoryName = summaryState.categoryName,
                    isTrollRound = summaryState.isTrollRound,
                    scoresAwarded = summaryState.scoresAwarded,
                    punishmentText = summaryState.punishmentText,
                    language = language,
                    onAwardScores = { crewmatesWon ->
                        viewModel.awardScores(crewmatesWon)
                    },
                    onPlayAgain = {
                        viewModel.playAgain()
                        // Start a new game with same setup — jump back to reveal
                        if (viewModel.startGame()) {
                            navController.navigate(Routes.REVEAL) {
                                popUpTo(Routes.HOME) { inclusive = false }
                            }
                        }
                    },
                    onNewGame = {
                        viewModel.newGame()
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = true }
                        }
                    }
                )
            }
        }

        // LEADERBOARD
        composable(Routes.LEADERBOARD) {
            val scores by viewModel.scores.collectAsState(initial = emptyMap())

            LeaderboardScreen(
                scores = scores,
                language = language,
                onClearScores = { viewModel.clearScores() },
                onBack = { navController.popBackStack() }
            )
        }

        // CUSTOM DECKS
        composable(Routes.CUSTOM_DECKS) {
            com.example.impostorgame.ui.screens.CustomDecksScreen(
                language = language,
                onBack = { navController.popBackStack() },
                onCategoriesUpdated = { viewModel.loadCategories() }
            )
        }

        // RULES
        composable(Routes.RULES) {
            RulesScreen(
                language = language,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
