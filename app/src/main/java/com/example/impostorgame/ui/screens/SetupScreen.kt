package com.example.impostorgame.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Category
import com.example.impostorgame.model.GameState
import com.example.impostorgame.model.Language
import com.example.impostorgame.ui.components.*
import com.example.impostorgame.ui.theme.*

/**
 * Game setup screen — configure players, impostors, categories, and game options.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun SetupScreen(
    setupState: GameState.Setup,
    language: Language,
    allCategories: List<Category>,
    onPlayerCountChange: (Int) -> Unit,
    onImpostorCountChange: (Int) -> Unit,
    onCategoryToggle: (String) -> Unit,
    onSelectAll: () -> Unit,
    onDeselectAll: () -> Unit,
    onSpyModeChange: (Boolean) -> Unit,
    onSpyHintLevelChange: (Int) -> Unit,
    onTrollModeChange: (Boolean) -> Unit,
    onPunishmentsEnabledChange: (Boolean) -> Unit,
    onCategoryHintChange: (Boolean) -> Unit,
    onTimerEnabledChange: (Boolean) -> Unit,
    onTimerChange: (Int) -> Unit,
    onPlayerNameChange: (Int, String) -> Unit,
    onStartGame: () -> Unit,
    onBack: () -> Unit
) {
    var showError by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 16.dp,
                bottom = 100.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextLight
                        )
                    }
                    Text(
                        text = Strings.gameSetup(language),
                        style = MaterialTheme.typography.headlineLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Players & Impostors card
            item {
                GlassCard {
                    CounterSelector(
                        label = Strings.players(language),
                        value = setupState.playerCount,
                        onIncrement = { onPlayerCountChange(setupState.playerCount + 1) },
                        onDecrement = { onPlayerCountChange(setupState.playerCount - 1) }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = DividerColor.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(16.dp))
                    CounterSelector(
                        label = Strings.impostors(language),
                        value = setupState.impostorCount,
                        onIncrement = { onImpostorCountChange(setupState.impostorCount + 1) },
                        onDecrement = { onImpostorCountChange(setupState.impostorCount - 1) }
                    )
                }
            }

            // Player nicknames card
            item {
                GlassCard {
                    Text(
                        text = Strings.playerNames(language),
                        style = MaterialTheme.typography.titleMedium,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    setupState.playerNames.forEachIndexed { index, name ->
                        OutlinedTextField(
                            value = name,
                            onValueChange = { onPlayerNameChange(index, it) },
                            label = {
                                Text("${Strings.playerLabel(language)} ${index + 1}")
                            },
                            placeholder = {
                                Text(Strings.nickname(language))
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextLight,
                                focusedBorderColor = PrimaryPurpleLight,
                                unfocusedBorderColor = DividerColor,
                                focusedLabelColor = PrimaryPurpleLight,
                                unfocusedLabelColor = TextMuted,
                                cursorColor = PrimaryPurpleLight
                            )
                        )
                    }
                }
            }

            // Timer card
            item {
                GlassCard {
                    ToggleOption(
                        label = Strings.timerEnabled(language),
                        description = Strings.timerEnabledDesc(language),
                        checked = setupState.timerEnabled,
                        onCheckedChange = onTimerEnabledChange
                    )
                    // Only show timer seconds when timer is enabled
                    androidx.compose.animation.AnimatedVisibility(
                        visible = setupState.timerEnabled
                    ) {
                        Column {
                            Spacer(modifier = Modifier.height(12.dp))
                            HorizontalDivider(color = DividerColor.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(12.dp))
                            CounterSelector(
                                label = Strings.timer(language),
                                value = setupState.timerSeconds,
                                onIncrement = { onTimerChange(setupState.timerSeconds + 30) },
                                onDecrement = { onTimerChange(setupState.timerSeconds - 30) }
                            )
                        }
                    }
                }
            }

            // Options card
            item {
                GlassCard {
                    ToggleOption(
                        label = Strings.spyMode(language),
                        description = Strings.spyModeDesc(language),
                        checked = setupState.spyModeEnabled,
                        onCheckedChange = onSpyModeChange
                    )
                    
                    AnimatedVisibility(visible = setupState.spyModeEnabled) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 16.dp, bottom = 8.dp)
                                .background(DarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = Strings.spyHintLabel(language),
                                style = MaterialTheme.typography.titleSmall,
                                color = TextWhite,
                                fontWeight = FontWeight.Bold
                            )
                            val options = Strings.spyHintOptions(language)
                            options.forEachIndexed { index, optionText ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (setupState.spyHintLevel == index) PrimaryPurple.copy(alpha = 0.2f) else androidx.compose.ui.graphics.Color.Transparent,
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable { onSpyHintLevelChange(index) }
                                        .padding(horizontal = 8.dp, vertical = 6.dp)
                                ) {
                                    RadioButton(
                                        selected = setupState.spyHintLevel == index,
                                        onClick = { onSpyHintLevelChange(index) },
                                        colors = RadioButtonDefaults.colors(selectedColor = PrimaryPurpleLight, unselectedColor = TextMuted)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = optionText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (setupState.spyHintLevel == index) TextWhite else TextMuted
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = DividerColor.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    ToggleOption(
                        label = Strings.trollMode(language),
                        description = Strings.trollModeDesc(language),
                        checked = setupState.trollModeEnabled,
                        onCheckedChange = onTrollModeChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = DividerColor.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    ToggleOption(
                        label = Strings.categoryHint(language),
                        description = Strings.categoryHintDesc(language),
                        checked = setupState.categoryHintEnabled,
                        onCheckedChange = onCategoryHintChange
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = DividerColor.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    ToggleOption(
                        label = Strings.enablePunishments(language),
                        description = Strings.enablePunishmentsDesc(language),
                        checked = setupState.punishmentsEnabled,
                        onCheckedChange = onPunishmentsEnabledChange
                    )
                }
            }

            // Categories section
            item {
                GlassCard {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = Strings.selectCategories(language),
                            style = MaterialTheme.typography.titleMedium,
                            color = TextWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            TextButton(onClick = onSelectAll) {
                                Text(
                                    text = if (language == Language.EN) "All" else "Sve",
                                    color = PrimaryPurpleLight,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                            TextButton(onClick = onDeselectAll) {
                                Text(
                                    text = if (language == Language.EN) "None" else "Ništa",
                                    color = TextMuted,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Category chips in a wrapping flow layout
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        allCategories.forEach { category ->
                            CategoryChip(
                                name = category.getName(language),
                                selected = category.id in setupState.selectedCategoryIds,
                                onClick = { onCategoryToggle(category.id) }
                            )
                        }
                    }

                    // Selected count
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "${setupState.selectedCategoryIds.size} / ${allCategories.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = TextMuted
                    )
                }
            }

            // Error message
            item {
                AnimatedVisibility(
                    visible = showError != null,
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut()
                ) {
                    showError?.let { error ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = ImpostorRed.copy(alpha = 0.2f)
                            )
                        ) {
                            Text(
                                text = error,
                                color = ImpostorRed,
                                modifier = Modifier.padding(16.dp),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground.copy(alpha = 0f),
                            DarkBackground.copy(alpha = 0.9f),
                            DarkBackground
                        )
                    )
                )
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            GameButton(
                text = Strings.startGame(language),
                onClick = {
                    when {
                        setupState.selectedCategoryIds.isEmpty() -> {
                            showError = Strings.minCategoriesError(language)
                        }
                        setupState.playerCount < 3 -> {
                            showError = Strings.minPlayersError(language)
                        }
                        else -> {
                            showError = null
                            onStartGame()
                        }
                    }
                }
            )
        }
    }
}
