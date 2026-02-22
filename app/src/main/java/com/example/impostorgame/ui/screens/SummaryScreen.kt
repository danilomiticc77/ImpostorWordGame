package com.example.impostorgame.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Language
import com.example.impostorgame.model.Player
import com.example.impostorgame.model.WordItem
import com.example.impostorgame.ui.components.GameButton
import com.example.impostorgame.ui.components.GameOutlinedButton
import com.example.impostorgame.ui.components.GlassCard
import com.example.impostorgame.ui.theme.*

/**
 * Summary screen — reveals the secret word, lists impostors, and indicates troll round.
 * Provides buttons to Play Again (same settings) or start a New Game.
 */
@Composable
fun SummaryScreen(
    players: List<Player>,
    secretWord: WordItem,
    categoryName: String,
    isTrollRound: Boolean,
    scoresAwarded: Boolean,
    punishmentText: String?,
    language: Language,
    onAwardScores: (Boolean) -> Unit,
    onPlayAgain: () -> Unit,
    onNewGame: () -> Unit
) {
    val impostors = players.filter { it.isImpostor }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface, DarkBackground)
                )
            )
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(vertical = 48.dp)
    ) {
            // Title
            item {
                Text(
                    text = Strings.gameOver(language),
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Troll round indicator
            if (isTrollRound) {
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = TrollGold.copy(alpha = 0.2f)
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = Strings.trollRound(language),
                            style = MaterialTheme.typography.titleMedium,
                            color = TrollGold,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        )
                    }
                }
            }

            // Secret word reveal
            item {
                GlassCard {
                    Text(
                        text = Strings.theSecretWord(language),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = secretWord.word,
                        style = MaterialTheme.typography.displayMedium,
                        fontWeight = FontWeight.Black,
                        color = SafeGreen,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "${Strings.categoryLabel(language)} $categoryName",
                        style = MaterialTheme.typography.bodyMedium,
                        color = AccentCyan,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Impostors list
            item {
                GlassCard {
                    Text(
                        text = Strings.impostorsWere(language),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextMuted,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    impostors.forEach { player ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .background(
                                    ImpostorRed.copy(alpha = 0.1f),
                                    RoundedCornerShape(12.dp)
                                )
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🕵️ ",
                                fontSize = 20.sp
                            )
                            Text(
                                text = player.name,
                                style = MaterialTheme.typography.titleMedium,
                                color = ImpostorRed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Winner / Scoring
            item {
                if (!scoresAwarded) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = Strings.whoWon(language),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            GameButton(
                                text = Strings.crewmatesWon(language),
                                onClick = { onAwardScores(true) },
                                modifier = Modifier.weight(1f)
                            )
                            GameButton(
                                text = Strings.impostorWon(language),
                                onClick = { onAwardScores(false) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                } else {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "✅ ${Strings.scoreSaved(language)}",
                            style = MaterialTheme.typography.titleMedium,
                            color = SafeGreen,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 16.dp)
                        )
                        
                        if (punishmentText != null) {
                            GlassCard {
                                Text(
                                    text = punishmentText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = AccentCyan,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }
                }
            }

            // Action buttons
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    GameButton(
                        text = Strings.playAgain(language),
                        onClick = onPlayAgain
                    )
                    GameOutlinedButton(
                        text = Strings.newGame(language),
                        onClick = onNewGame
                    )
                }
        }
    }
}
