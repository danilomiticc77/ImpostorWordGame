package com.example.impostorgame.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.impostorgame.data.CustomCategoryManager
import com.example.impostorgame.data.Strings
import com.example.impostorgame.model.Category
import com.example.impostorgame.model.Language
import com.example.impostorgame.model.WordItem
import com.example.impostorgame.ui.components.GameButton
import com.example.impostorgame.ui.components.GameOutlinedButton
import com.example.impostorgame.ui.components.GlassCard
import com.example.impostorgame.ui.theme.*
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDecksScreen(
    language: Language,
    onBack: () -> Unit,
    onCategoriesUpdated: () -> Unit
) {
    val context = LocalContext.current
    var customCategories by remember { mutableStateOf(CustomCategoryManager.getCustomCategories(context)) }
    var isCreating by remember { mutableStateOf(false) }

    // Form states
    var newDeckName by remember { mutableStateOf("") }
    var newDeckWords by remember { mutableStateOf("") }

    fun refresh() {
        customCategories = CustomCategoryManager.getCustomCategories(context)
        onCategoriesUpdated()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(Strings.customDecks(language), color = TextWhite, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (isCreating) {
                GlassCard {
                    Text(
                        text = if (language == Language.EN) "Create New Deck" else "Kreiraj novu kategoriju",
                        style = MaterialTheme.typography.titleLarge,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = newDeckName,
                        onValueChange = { newDeckName = it },
                        label = { Text(if (language == Language.EN) "Deck Name" else "Ime kategorije") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurpleLight,
                            focusedLabelColor = PrimaryPurpleLight,
                            unfocusedBorderColor = DividerColor
                        ),
                        singleLine = true
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = newDeckWords,
                        onValueChange = { newDeckWords = it },
                        label = { Text(if (language == Language.EN) "Words (comma separated)" else "Reči (odvojene zarezom)") },
                        modifier = Modifier.fillMaxWidth().height(120.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryPurpleLight,
                            focusedLabelColor = PrimaryPurpleLight,
                            unfocusedBorderColor = DividerColor
                        ),
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        GameButton(
                            text = if (language == Language.EN) "Save" else "Sačuvaj",
                            onClick = {
                                val name = newDeckName.trim()
                                val wordsList = newDeckWords.split(",").map { it.trim() }.filter { it.isNotEmpty() }
                                
                                if (name.isBlank() || wordsList.size < 3) {
                                    Toast.makeText(context, if (language == Language.EN) "Need name and at least 3 words!" else "Unesite ime i bar 3 reči!", Toast.LENGTH_SHORT).show()
                                    return@GameButton
                                }
                                
                                val wordItems = wordsList.map { WordItem(it, "") }
                                val newCat = Category(
                                    id = "custom_${UUID.randomUUID()}",
                                    nameEn = name,
                                    nameSr = name,
                                    wordsEn = wordItems,
                                    wordsSr = wordItems
                                )
                                CustomCategoryManager.saveCustomCategory(context, newCat)
                                refresh()
                                isCreating = false
                                newDeckName = ""
                                newDeckWords = ""
                            },
                            modifier = Modifier.weight(1f)
                        )
                        GameOutlinedButton(
                            text = if (language == Language.EN) "Cancel" else "Poništi",
                            onClick = { isCreating = false },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            } else {
                if (customCategories.isEmpty()) {
                    Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text(
                            text = if (language == Language.EN) "No custom decks yet.\nCreate one!" else "Još nema sopstvenih reči.\nKreiraj ih!",
                            color = TextMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(customCategories) { cat ->
                            GlassCard {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(text = cat.nameEn, color = TextWhite, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                                        Text(text = "${cat.wordsEn.size} ${if (language == Language.EN) "words" else "reči"}", color = TextMuted, style = MaterialTheme.typography.bodyMedium)
                                    }
                                    IconButton(
                                        onClick = {
                                            CustomCategoryManager.deleteCustomCategory(context, cat.id)
                                            refresh()
                                        }
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = ImpostorRed)
                                    }
                                }
                            }
                        }
                    }
                }

                GameButton(
                    text = if (language == Language.EN) "Create New Deck" else "Nova Kategorija",
                    onClick = { isCreating = true }
                )
            }
        }
    }
}
