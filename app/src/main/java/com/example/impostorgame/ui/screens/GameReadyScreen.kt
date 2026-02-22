package com.example.impostorgame.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Language
import com.example.impostorgame.ui.components.GameButton
import com.example.impostorgame.ui.components.GlassCard
import com.example.impostorgame.ui.theme.*

/**
 * Discussion screen — timer (if enabled) and a button to reveal the impostor.
 */
@Composable
fun GameReadyScreen(
    timerEnabled: Boolean,
    timerSeconds: Int,
    timerRunning: Boolean,
    starterPlayerName: String,
    language: Language,
    onStartTimer: () -> Unit,
    onRevealImpostor: () -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }

    // Pulse animation for the timer circle
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (timerRunning && timerSeconds > 0) 1.08f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = EaseInOutCubic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    // Format timer as MM:SS
    val minutes = timerSeconds / 60
    val seconds = timerSeconds % 60
    val timeText = String.format("%02d:%02d", minutes, seconds)

    // Color shifts when time is running low
    val timerColor = when {
        timerSeconds <= 0 -> TextMuted
        timerSeconds <= 10 -> ImpostorRed
        timerSeconds <= 30 -> TrollGold
        else -> PrimaryPurpleLight
    }

    // Confirmation dialog
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(
                    text = "🕵️ ${Strings.revealImpostor(language)}",
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    text = Strings.areYouSure(language),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmDialog = false
                        onRevealImpostor()
                    }
                ) {
                    Text(
                        text = Strings.yes(language),
                        color = ImpostorRed,
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text(
                        text = Strings.cancelText(language),
                        color = TextMuted
                    )
                }
            },
            containerColor = DarkSurface,
            titleContentColor = TextWhite,
            textContentColor = TextMuted,
            shape = RoundedCornerShape(20.dp)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface, DarkBackground)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 48.dp, horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = Strings.discussionTime(language),
                style = MaterialTheme.typography.displaySmall,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            if (timerEnabled) {
                // Timer circle
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .scale(pulseScale)
                        .background(
                            Brush.radialGradient(
                                colors = listOf(
                                    timerColor.copy(alpha = 0.2f),
                                    timerColor.copy(alpha = 0.05f),
                                    DarkBackground.copy(alpha = 0f)
                                )
                            ),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(200.dp)
                            .background(
                                DarkSurface.copy(alpha = 0.8f),
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = timeText,
                            fontSize = 56.sp,
                            fontWeight = FontWeight.Black,
                            color = timerColor
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (!timerRunning && timerSeconds > 0) {
                    GameButton(
                        text = Strings.tapToStart(language),
                        onClick = onStartTimer,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                } else {
                    StarterPlayerCard(starterPlayerName, language)
                    Spacer(modifier = Modifier.height(24.dp))
                    GameButton(
                        text = "🕵️ ${Strings.revealImpostor(language)}",
                        onClick = { showConfirmDialog = true },
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            } else {
                StarterPlayerCard(starterPlayerName, language)
                Spacer(modifier = Modifier.height(24.dp))
                GameButton(
                    text = "🕵️ ${Strings.revealImpostor(language)}",
                    onClick = { showConfirmDialog = true },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun StarterPlayerCard(
    playerName: String,
    language: Language
) {
    GlassCard(
        modifier = Modifier.fillMaxWidth(0.85f)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (language == Language.EN) "Starts first:" else "Prvi igra:",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "🎯 $playerName",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = AccentCyan,
                textAlign = TextAlign.Center
            )
        }
    }
}
