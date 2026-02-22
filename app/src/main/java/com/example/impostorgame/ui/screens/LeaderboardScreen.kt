package com.example.impostorgame.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Language
import com.example.impostorgame.ui.components.GlassCard
import com.example.impostorgame.ui.theme.*
import androidx.compose.ui.text.style.TextAlign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(
    scores: Map<String, Int>,
    language: Language,
    onClearScores: () -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.leaderboard(language)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (scores.isNotEmpty()) {
                        IconButton(onClick = onClearScores) {
                            Icon(Icons.Default.Delete, contentDescription = "Clear", tint = ImpostorRed)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = TextWhite,
                    navigationIconContentColor = TextWhite
                )
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(DarkBackground, DarkSurface, DarkBackground)
                    )
                )
        ) {
            if (scores.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    GlassCard {
                        Text(
                            text = Strings.scoringRules(language),
                            color = TextMuted,
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        Text(
                            text = Strings.noScores(language),
                            color = TextMuted,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        GlassCard {
                            Text(
                                text = Strings.scoringRules(language),
                                color = TextMuted,
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                    itemsIndexed(scores.entries.toList()) { index, entry ->
                        val rankFormat = when (index) {
                            0 -> "🥇"
                            1 -> "🥈"
                            2 -> "🥉"
                            else -> "${index + 1}."
                        }
                        
                        val rankColor = when (index) {
                            0 -> TrollGold
                            1 -> TextWhite
                            2 -> AccentCyan
                            else -> TextMuted
                        }

                        GlassCard {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp, horizontal = 16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                    Text(
                                        text = rankFormat,
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = rankColor,
                                        modifier = Modifier.width(40.dp)
                                    )
                                    Text(
                                        text = entry.key,
                                        style = MaterialTheme.typography.titleLarge,
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                
                                Surface(
                                    color = PrimaryPurple.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text(
                                        text = Strings.points(language, entry.value),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = PrimaryPurpleLight,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
