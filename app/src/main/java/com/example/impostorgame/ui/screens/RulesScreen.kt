package com.example.impostorgame.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.impostorgame.model.Language
import com.example.impostorgame.ui.components.GlassCard
import com.example.impostorgame.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RulesScreen(
    language: Language,
    onBack: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(DarkBackground, DarkSurface, DarkBackground)
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 80.dp,
                bottom = 32.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ====== HOW TO PLAY ======
            item {
                RuleCard(
                    emoji = "🎮",
                    title = if (language == Language.EN) "How to Play" else "Kako se igra",
                    content = if (language == Language.EN) {
                        "1. Choose the number of players and categories.\n" +
                        "2. Pass the phone around — each player holds the screen to see their card.\n" +
                        "3. Crewmates see the SECRET WORD. Impostors see only a hint.\n" +
                        "4. Discuss! Ask questions to figure out who doesn't know the word.\n" +
                        "5. Vote to find the Impostor!"
                    } else {
                        "1. Izaberi broj igrača i kategorije.\n" +
                        "2. Dodajte telefon u krug — svaki igrač drži ekran da vidi svoju kartu.\n" +
                        "3. Crewmejtovi vide TAJNU REČ. Impostori vide samo hint.\n" +
                        "4. Diskutujte! Postavljajte pitanja da otkrijete ko ne zna reč.\n" +
                        "5. Glasajte da pronađete Impostora!"
                    }
                )
            }

            // ====== ROLES ======
            item {
                Text(
                    text = if (language == Language.EN) "👥 Roles" else "👥 Uloge",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Crewmate
            item {
                RuleCard(
                    emoji = "🟢",
                    title = if (language == Language.EN) "Crewmate" else "Crewmejt",
                    content = if (language == Language.EN) {
                        "You see the secret word. Your goal is to find the Impostor by asking clever questions — but be careful not to reveal the word!"
                    } else {
                        "Vidiš tajnu reč. Tvoj cilj je da pronađeš Impostora postavljajući pametna pitanja — ali pazi da ne otkriješ reč!"
                    }
                )
            }

            // Impostor
            item {
                RuleCard(
                    emoji = "🔴",
                    title = "Impostor",
                    content = if (language == Language.EN) {
                        "You DON'T know the secret word — you only get a vague hint. Blend in with the crewmates, pretend you know the word, and try not to get caught!"
                    } else {
                        "NE znaš tajnu reč — dobijaš samo nejasan hint. Uklopi se sa crewmejtovima, pravi se da znaš reč i pokušaj da te ne otkriju!"
                    }
                )
            }

            // Spy
            item {
                RuleCard(
                    emoji = "🕵️",
                    title = if (language == Language.EN) "Spy" else "Špijun",
                    content = if (language == Language.EN) {
                        "You know who the Impostor is! Depending on settings, you might also see the word, a hint, or the category. Your secret mission: help the Impostor win WITHOUT getting caught yourself!"
                    } else {
                        "Znaš ko je Impostor! U zavisnosti od podešavanja, možeš videti reč, hint ili kategoriju. Tvoja tajna misija: pomozi Impostoru da pobedi BEZ da te otkriju!"
                    }
                )
            }

            // ====== GAME MODES ======
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == Language.EN) "⚙️ Game Modes" else "⚙️ Modovi igre",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // Spy Mode
            item {
                RuleCard(
                    emoji = "🕵️",
                    title = if (language == Language.EN) "Spy Mode" else "Špijun mod",
                    content = if (language == Language.EN) {
                        "Adds a Spy to the game. The Spy knows who the Impostor is and secretly helps them. You can choose what info the Spy gets:\n\n" +
                        "📜 Sees the Word — easiest for the Spy\n" +
                        "📁 Sees Category — knows the topic\n" +
                        "🔍 Sees Hint — same clue as the Impostor\n" +
                        "🔍📁 Hint + Category — maximum info"
                    } else {
                        "Dodaje Špijuna u igru. Špijun zna ko je Impostor i tajno mu pomaže. Možeš da biraš šta Špijun vidi:\n\n" +
                        "📜 Vidi reč — najlakše za Špijuna\n" +
                        "📁 Vidi kategoriju — zna temu\n" +
                        "🔍 Vidi hint — isti trag kao Impostor\n" +
                        "🔍📁 Hint + kategorija — maksimum info"
                    }
                )
            }

            // Troll Mode
            item {
                RuleCard(
                    emoji = "🤡",
                    title = if (language == Language.EN) "Troll Mode" else "Trol mod",
                    content = if (language == Language.EN) {
                        "Sometimes chaos is fun! When enabled, there's a ~10% chance that a round becomes a TROLL ROUND — everyone is an impostor and nobody knows the real word. Pure madness!"
                    } else {
                        "Ponekad je haos zabavan! Kada je uključen, postoji ~10% šanse da runda postane TROL RUNDA — svi su impostori i niko ne zna pravu reč. Čist haos!"
                    }
                )
            }

            // Category Hint
            item {
                RuleCard(
                    emoji = "📁",
                    title = if (language == Language.EN) "Category Hint" else "Hint kategorije",
                    content = if (language == Language.EN) {
                        "When enabled, the Impostor also sees which category the word belongs to (e.g. \"Animals\", \"Food\"). This makes it slightly easier for the Impostor to fake it."
                    } else {
                        "Kada je uključeno, Impostor takođe vidi kojoj kategoriji reč pripada (npr. \"Životinje\", \"Hrana\"). Ovo malo olakšava Impostoru da se uklopi."
                    }
                )
            }

            // Punishment Roulette
            item {
                RuleCard(
                    emoji = "🎰",
                    title = if (language == Language.EN) "Punishment Roulette" else "Rulet kazni",
                    content = if (language == Language.EN) {
                        "After the round ends and you decide the winner, the losing team gets a random fun punishment! Things like singing, push-ups, or picking the next song. Party vibes only! 🎉"
                    } else {
                        "Nakon što se runda završi i odlučite pobednika, gubitnički tim dobija nasumičnu zabavnu kaznu! Stvari poput pevanja, sklekova ili biranja sledeće pesme. Samo za dobru atmosferu! 🎉"
                    }
                )
            }

            // Timer
            item {
                RuleCard(
                    emoji = "⏱️",
                    title = if (language == Language.EN) "Discussion Timer" else "Tajmer za diskusiju",
                    content = if (language == Language.EN) {
                        "Set a countdown timer for discussions. When enabled, a timer counts down during the discussion phase. You can customize the duration in seconds."
                    } else {
                        "Postavi odbrojavanje za diskusiju. Kada je uključen, tajmer odbrojava tokom faze diskusije. Možeš da prilagodiš trajanje u sekundama."
                    }
                )
            }

            // ====== SCORING ======
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = if (language == Language.EN) "🏆 Scoring" else "🏆 Bodovanje",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            item {
                RuleCard(
                    emoji = "⭐",
                    title = if (language == Language.EN) "Points" else "Poeni",
                    content = if (language == Language.EN) {
                        "🏆 If crewmates win → each crewmate gets +1 point\n" +
                        "🔪 If impostor wins → impostor (+ spy) get +2 points\n\n" +
                        "Scores are saved and shown on the Leaderboard!"
                    } else {
                        "🏆 Ako crewmejtovi pobede → svaki crewmejt dobija +1 bod\n" +
                        "🔪 Ako impostor pobedi → impostor (+ špijun) dobijaju +2 boda\n\n" +
                        "Rezultati se čuvaju i prikazuju na tabeli lidera!"
                    }
                )
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Top bar
        TopAppBar(
            title = {
                Text(
                    text = if (language == Language.EN) "Rules" else "Pravila",
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            },
            navigationIcon = {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = TextWhite
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = DarkBackground.copy(alpha = 0.9f)
            )
        )
    }
}

@Composable
private fun RuleCard(
    emoji: String,
    title: String,
    content: String
) {
    GlassCard {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = emoji,
                fontSize = 28.sp,
                modifier = Modifier.padding(end = 12.dp, top = 2.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryPurpleLight
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = content,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextLight,
                    lineHeight = 22.sp
                )
            }
        }
    }
}
