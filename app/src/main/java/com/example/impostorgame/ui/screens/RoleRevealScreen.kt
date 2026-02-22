package com.example.impostorgame.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Language
import com.example.impostorgame.ui.components.GameButton
import com.example.impostorgame.ui.theme.*
import com.example.impostorgame.viewmodel.RevealContent

/**
 * Role reveal screen — hold to see your role, release to hide.
 * After viewing, a "Next Player" button appears below the card.
 */
@Composable
fun RoleRevealScreen(
    playerName: String,
    playerNumber: Int,
    totalPlayers: Int,
    language: Language,
    isRevealing: Boolean,
    hasViewed: Boolean,
    revealContent: RevealContent?,
    onPressDown: () -> Unit,
    onPressUp: () -> Unit,
    onNextPlayer: () -> Unit
) {
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
                .padding(top = 48.dp, bottom = 32.dp, start = 24.dp, end = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Header
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "$playerNumber / $totalPlayers",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )
                Text(
                    text = playerName,
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )
            }

            // Card area (flexible space, so it never resizes when the footer changes)
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                RevealBox(
                    playerNumber = playerNumber,
                    language = language,
                    isRevealing = isRevealing,
                    revealContent = revealContent,
                    onPressDown = onPressDown,
                    onPressUp = onPressUp
                )
            }

            // Footer - fixed height to ensure it doesn't shift the card above it when appearing
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.animation.AnimatedVisibility(
                    visible = hasViewed && !isRevealing,
                    enter = fadeIn(tween(300)) + slideInVertically(tween(300)),
                    exit = fadeOut(tween(100))
                ) {
                    GameButton(
                        text = if (playerNumber < totalPlayers) {
                            Strings.passToNextPlayer(language)
                        } else {
                            Strings.startDiscussion(language)
                        },
                        onClick = onNextPlayer,
                        modifier = Modifier.fillMaxWidth(0.7f)
                    )
                }
            }
        }
    }
}

@Composable
private fun RevealBox(
    playerNumber: Int,
    language: Language,
    isRevealing: Boolean,
    revealContent: RevealContent?,
    onPressDown: () -> Unit,
    onPressUp: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth(0.95f)
            .pointerInput(playerNumber) {
                detectTapGestures(
                    onPress = {
                        onPressDown()
                        tryAwaitRelease()
                        onPressUp()
                    }
                )
            }
            .background(
                if (isRevealing) {
                    if (revealContent?.isImpostor == true) ImpostorRed.copy(alpha = 0.15f)
                    else SafeGreen.copy(alpha = 0.1f)
                } else {
                    PrimaryPurple.copy(alpha = 0.2f)
                },
                shape = RoundedCornerShape(20.dp)
            )
            .padding(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // Hidden state prompt
        androidx.compose.animation.AnimatedVisibility(
            visible = !isRevealing,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(100))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "👆",
                    fontSize = 48.sp
                )
                Text(
                    text = Strings.holdToReveal(language),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurpleLight,
                    textAlign = TextAlign.Center
                )
            }
        }

        // Revealed content
        androidx.compose.animation.AnimatedVisibility(
            visible = isRevealing,
            enter = fadeIn(tween(150)),
            exit = fadeOut(tween(100))
        ) {
            revealContent?.let { content ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Role icon
                    Text(
                        text = if (content.isImpostor) "🕵️" else "✅",
                        fontSize = 56.sp
                    )

                    // Title
                    Text(
                        text = content.title,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = if (content.isImpostor) ImpostorRed else SafeGreen,
                        textAlign = TextAlign.Center
                    )

                    // Main text (word or hint)
                    Text(
                        text = content.mainText,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Black,
                        color = TextWhite,
                        textAlign = TextAlign.Center
                    )

                    // Category hint if enabled
                    content.categoryText?.let { cat ->
                        Text(
                            text = cat,
                            style = MaterialTheme.typography.bodyLarge,
                            color = AccentCyan,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
