package com.example.impostorgame.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Language
import com.example.impostorgame.ui.components.CategoryChip
import com.example.impostorgame.ui.components.GameButton
import com.example.impostorgame.ui.components.GameOutlinedButton
import com.example.impostorgame.ui.components.ToggleOption
import com.example.impostorgame.ui.theme.*

/**
 * Home screen — app title, language toggle, start button.
 * First thing the user sees when opening the app.
 */
@Composable
fun HomeScreen(
    language: Language,
    onLanguageChange: (Language) -> Unit,
    onStartClick: () -> Unit,
    onLeaderboardClick: () -> Unit,
    onCustomDecksClick: () -> Unit,
    onRulesClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface, DarkBackground)
                )
            )
            .systemBarsPadding(),
        contentPadding = PaddingValues(horizontal = 40.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // App icon
        item {
            androidx.compose.foundation.Image(
                painter = painterResource(id = com.example.impostorgame.R.drawable.impostor),
                contentDescription = "Impostor Game Icon",
                modifier = Modifier.size(120.dp)
            )
        }

        // App title
        item {
            Text(
                text = Strings.appTitle(language),
                style = MaterialTheme.typography.displayMedium,
                fontWeight = FontWeight.Black,
                color = TextWhite,
                textAlign = TextAlign.Center
            )
        }

        // Subtitle
        item {
            Text(
                text = when (language) {
                    Language.EN -> "Find the impostor among your friends!"
                    Language.SR -> "Pronađi impostora među prijateljima!"
                },
                style = MaterialTheme.typography.bodyLarge,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }

        // Language selector
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Language.entries.forEach { lang ->
                    val isSelected = lang == language
                    FilterChip(
                        selected = isSelected,
                        onClick = { onLanguageChange(lang) },
                        label = {
                            Text(
                                text = "${lang.flag} ${lang.displayName}",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = PrimaryPurple.copy(alpha = 0.4f),
                            selectedLabelColor = PrimaryPurpleLight,
                            containerColor = DarkSurfaceVariant,
                            labelColor = TextMuted
                        )
                    )
                }
            }
        }

        // Buttons
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Start button
                GameButton(
                    text = Strings.startGame(language),
                    onClick = onStartClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                
                // Leaderboard button
                GameOutlinedButton(
                    text = Strings.leaderboard(language),
                    onClick = onLeaderboardClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                // Custom Decks button
                GameOutlinedButton(
                    text = Strings.customDecks(language),
                    onClick = onCustomDecksClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )

                // Rules button
                GameOutlinedButton(
                    text = if (language == Language.EN) "📖 Rules" else "📖 Pravila",
                    onClick = onRulesClick,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
        }
    }
}
