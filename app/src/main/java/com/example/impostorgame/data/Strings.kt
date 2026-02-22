package com.example.impostorgame.data

import com.example.impostorgame.model.Language

/**
 * All UI strings in both English and Serbian.
 * Centralized localization — no Android resources needed.
 */
object Strings {

    fun appTitle(lang: Language) = when (lang) {
        Language.EN -> "Impostor Word Game"
        Language.SR -> "Impostor Igra Reči"
    }

    fun startGame(lang: Language) = when (lang) {
        Language.EN -> "Start Game"
        Language.SR -> "Započni Igru"
    }

    fun players(lang: Language) = when (lang) {
        Language.EN -> "Players"
        Language.SR -> "Igrači"
    }

    fun impostors(lang: Language) = when (lang) {
        Language.EN -> "Impostors"
        Language.SR -> "Impostori"
    }

    fun categories(lang: Language) = when (lang) {
        Language.EN -> "Categories"
        Language.SR -> "Kategorije"
    }

    fun selectCategories(lang: Language) = when (lang) {
        Language.EN -> "Select Categories"
        Language.SR -> "Izaberi Kategorije"
    }

    fun trollMode(lang: Language) = when (lang) {
        Language.EN -> "Troll Mode"
        Language.SR -> "Troll Mod"
    }

    fun trollModeDesc(lang: Language) = when (lang) {
        Language.EN -> "~10% chance everyone is an impostor!"
        Language.SR -> "~10% šansa da svi budu impostori!"
    }

    fun categoryHint(lang: Language) = when (lang) {
        Language.EN -> "Category Hint"
        Language.SR -> "Hint Kategorije"
    }

    fun categoryHintDesc(lang: Language) = when (lang) {
        Language.EN -> "Impostors can see which category"
        Language.SR -> "Impostori vide koja je kategorija"
    }

    fun player(lang: Language, number: Int) = when (lang) {
        Language.EN -> "Player $number"
        Language.SR -> "Igrač $number"
    }

    fun playerLabel(lang: Language) = when (lang) {
        Language.EN -> "Player"
        Language.SR -> "Igrač"
    }

    fun nickname(lang: Language) = when (lang) {
        Language.EN -> "Nickname"
        Language.SR -> "Nadimak"
    }

    fun playerNames(lang: Language) = when (lang) {
        Language.EN -> "Player Names"
        Language.SR -> "Imena Igrača"
    }

    fun holdToReveal(lang: Language) = when (lang) {
        Language.EN -> "Hold to Reveal"
        Language.SR -> "Drži za Otkrivanje"
    }

    fun youAreImpostor(lang: Language) = when (lang) {
        Language.EN -> "You are the Impostor!"
        Language.SR -> "Ti si Impostor!"
    }

    fun theWordIs(lang: Language) = when (lang) {
        Language.EN -> "The word is:"
        Language.SR -> "Reč je:"
    }

    fun hintLabel(lang: Language) = when (lang) {
        Language.EN -> "Hint:"
        Language.SR -> "Hint:"
    }

    fun categoryLabel(lang: Language) = when (lang) {
        Language.EN -> "Category:"
        Language.SR -> "Kategorija:"
    }

    fun passToNextPlayer(lang: Language) = when (lang) {
        Language.EN -> "Pass to Next Player"
        Language.SR -> "Prosledi Sledećem Igraču"
    }

    fun discussionTime(lang: Language) = when (lang) {
        Language.EN -> "Discussion Time!"
        Language.SR -> "Vreme za Diskusiju!"
    }

    fun endDiscussion(lang: Language) = when (lang) {
        Language.EN -> "End Discussion"
        Language.SR -> "Završi Diskusiju"
    }

    fun gameOver(lang: Language) = when (lang) {
        Language.EN -> "Game Over"
        Language.SR -> "Kraj Igre"
    }

    fun theSecretWord(lang: Language) = when (lang) {
        Language.EN -> "The Secret Word"
        Language.SR -> "Tajna Reč"
    }

    fun impostorsWere(lang: Language) = when (lang) {
        Language.EN -> "The Impostors Were:"
        Language.SR -> "Impostori su Bili:"
    }

    fun trollRound(lang: Language) = when (lang) {
        Language.EN -> "🎭 TROLL ROUND! Everyone was an impostor!"
        Language.SR -> "🎭 TROLL RUNDA! Svi su bili impostori!"
    }

    fun playAgain(lang: Language) = when (lang) {
        Language.EN -> "Play Again"
        Language.SR -> "Igraj Ponovo"
    }

    fun newGame(lang: Language) = when (lang) {
        Language.EN -> "New Game"
        Language.SR -> "Nova Igra"
    }

    fun timer(lang: Language) = when (lang) {
        Language.EN -> "Timer (seconds)"
        Language.SR -> "Tajmer (sekunde)"
    }

    fun timerEnabled(lang: Language) = when (lang) {
        Language.EN -> "Discussion Timer"
        Language.SR -> "Tajmer za Diskusiju"
    }

    fun timerEnabledDesc(lang: Language) = when (lang) {
        Language.EN -> "Enable countdown timer before voting"
        Language.SR -> "Uključi odbrojavanje pre glasanja"
    }

    fun ready(lang: Language) = when (lang) {
        Language.EN -> "Ready?"
        Language.SR -> "Spremni?"
    }

    fun tapToStart(lang: Language) = when (lang) {
        Language.EN -> "Tap to Start"
        Language.SR -> "Tapni za Početak"
    }

    fun minPlayersError(lang: Language) = when (lang) {
        Language.EN -> "Minimum 3 players required"
        Language.SR -> "Minimum 3 igrača"
    }

    fun minCategoriesError(lang: Language) = when (lang) {
        Language.EN -> "Select at least 1 category"
        Language.SR -> "Izaberi bar 1 kategoriju"
    }

    fun settings(lang: Language) = when (lang) {
        Language.EN -> "Settings"
        Language.SR -> "Podešavanja"
    }

    fun tapToHide(lang: Language) = when (lang) {
        Language.EN -> "Tap to Hide"
        Language.SR -> "Tapni da Sakriješ"
    }

    fun everyoneReady(lang: Language) = when (lang) {
        Language.EN -> "Everyone has seen their role!"
        Language.SR -> "Svi su videli svoju ulogu!"
    }

    fun startDiscussion(lang: Language) = when (lang) {
        Language.EN -> "Start Discussion"
        Language.SR -> "Započni Diskusiju"
    }

    fun gameSetup(lang: Language) = when (lang) {
        Language.EN -> "Game Setup"
        Language.SR -> "Podešavanje Igre"
    }

    fun revealImpostor(lang: Language) = when (lang) {
        Language.EN -> "Reveal Impostor"
        Language.SR -> "Otkrij Impostora"
    }

    fun areYouSure(lang: Language) = when (lang) {
        Language.EN -> "Are you sure you want to reveal the impostor?"
        Language.SR -> "Da li ste sigurni da želite da otkrijete impostora?"
    }

    fun yes(lang: Language) = when (lang) {
        Language.EN -> "Yes"
        Language.SR -> "Da"
    }

    fun cancelText(lang: Language) = when (lang) {
        Language.EN -> "Cancel"
        Language.SR -> "Otkaži"
    }

    fun holdToRevealImpostor(lang: Language) = when (lang) {
        Language.EN -> "Hold to Reveal Impostor"
        Language.SR -> "Drži za Otkrivanje Impostora"
    }

    fun continueText(lang: Language) = when (lang) {
        Language.EN -> "Continue"
        Language.SR -> "Nastavi"
    }

    fun impostorIs(lang: Language) = when (lang) {
        Language.EN -> "The Impostor is:"
        Language.SR -> "Impostor je:"
    }

    fun impostorsAre(lang: Language) = when (lang) {
        Language.EN -> "The Impostors are:"
        Language.SR -> "Impostori su:"
    }

    fun leaderboard(lang: Language) = when (lang) {
        Language.EN -> "Leaderboard"
        Language.SR -> "Tabela Lidera"
    }

    fun noScores(lang: Language) = when (lang) {
        Language.EN -> "No scores yet. Play a game!"
        Language.SR -> "Nema rezultata. Odigrajte partiju!"
    }

    fun points(lang: Language, count: Int) = when (lang) {
        Language.EN -> if (count == 1) "1 pt" else "$count pts"
        Language.SR -> "$count bod"
    }

    fun whoWon(lang: Language) = when (lang) {
        Language.EN -> "Who Won?"
        Language.SR -> "Ko je pobedio?"
    }

    fun crewmatesWon(lang: Language) = when (lang) {
        Language.EN -> "Crewmates win! (+1)"
        Language.SR -> "Crewmejtovi su pobedili! (+1)"
    }

    fun impostorWon(lang: Language) = when (lang) {
        Language.EN -> "Impostor Wins! (+2)"
        Language.SR -> "Impostor pobeđuje! (+2)"
    }

    fun scoringRules(lang: Language) = when (lang) {
        Language.EN -> "🏆 If the crewmates win → each crewmate gets +1 point.\n🔪 If the impostor wins → impostor (+ spy) get +2 points."
        Language.SR -> "🏆 Ako crewmates pobede → svaki crewmate dobija +1 bod.\n🔪 Ako impostor pobedi → impostor (+ špijun) dobijaju +2 boda."
    }

    fun scoreSaved(lang: Language) = when (lang) {
        Language.EN -> "Scores updated!"
        Language.SR -> "Rezultati sačuvani!"
    }

    fun clearScores(lang: Language) = when (lang) {
        Language.EN -> "Clear Scores"
        Language.SR -> "Obriši rezultate"
    }

    fun spyMode(lang: Language) = when (lang) {
        Language.EN -> "Enable Spy"
        Language.SR -> "Uključi Špijuna"
    }

    fun spyModeDesc(lang: Language) = when (lang) {
        Language.EN -> "Spy knows the Impostor, but not the word. Helps Impostor. Automatically skipped during Troll rounds."
        Language.SR -> "Špijun zna ko je Impostor ali ne i reč. Pomaže Impostoru. Automatski se preskače u Troll rundama."
    }

    fun spyHintLabel(lang: Language) = when (lang) {
        Language.EN -> "Spy Info Level"
        Language.SR -> "Nivo informacija špijuna"
    }

    fun spyHintOptions(lang: Language): List<String> = when (lang) {
        Language.EN -> listOf(
            "📜 Sees the Word",
            "📁 Sees Category",
            "🔍 Sees Hint",
            "🔍📁 Hint + Category"
        )
        Language.SR -> listOf(
            "📜 Vidi reč",
            "📁 Vidi kategoriju",
            "🔍 Vidi hint",
            "🔍📁 Hint + kategorija"
        )
    }

    fun youAreSpy(lang: Language) = when (lang) {
        Language.EN -> "You are the Spy!"
        Language.SR -> "Ti si Špijun!"
    }

    fun spySeesImpostor(lang: Language, impostorName: String) = when (lang) {
        Language.EN -> "The Impostor is: $impostorName.\nSecretly help them!"
        Language.SR -> "Impostor je: $impostorName.\nTajno mu pomaži!"
    }

    // PUNISHMENTS
    fun enablePunishments(lang: Language) = when (lang) {
        Language.EN -> "Punishment Roulette"
        Language.SR -> "Kazneni Rulet"
    }

    fun enablePunishmentsDesc(lang: Language) = when (lang) {
        Language.EN -> "Losers get a random fun party punishment at the end of the round."
        Language.SR -> "Gubitnici dobijaju nasumičnu kaznu na kraju runde."
    }

    fun punishmentFor(lang: Language, who: String) = when (lang) {
        Language.EN -> "Punishment for $who:"
        Language.SR -> "Kazna za $who:"
    }

    fun theCrewmates(lang: Language) = when (lang) {
        Language.EN -> "Crewmates"
        Language.SR -> "Seljane"
    }

    fun theImpostor(lang: Language) = when (lang) {
        Language.EN -> "Impostor(s)"
        Language.SR -> "Impostora"
    }

    // CUSTOM DECKS
    fun customDecks(lang: Language) = when (lang) {
        Language.EN -> "Custom Decks"
        Language.SR -> "Sopstvene Reči"
    }

    fun getRandomPunishment(lang: Language): String {
        val list = when (lang) {
            Language.EN -> listOf(
    
                "Drink a full glass of water without stopping.",
                "Let the winners choose your next song on YouTube.",
                "Speak with a funny accent for the next round.",
                "Draw a tiny mustache on your finger and hold it up when speaking.",
                "Tell an embarrassing story.",
                "Do a silly dance for 15 seconds.",
                "Act like a chicken until the next round starts.",
                "Give a genuine compliment to the person on your left.",
                "Post a funny status and send it to the group.",
                "Daj pobednicima da krekuju",
                "Oil up lil bro"
            )
            Language.SR -> listOf(
                
                "Popij punu čašu na eks.",
                "Pobednici biraju sledeću pesmu na YouTube-u.",
                "Pričaj sa smešnim akcentom sledeću rundu.",
                "Nacrtaj mali brk na prstu i drži ga ispod nosa dok pričaš.",
                "Ispričaj jednu blamantnu priču o sebi.",
                "Odigraj jedan smešan ples 15 sekundi.",
                "Ponašaj se kao kokoška dok ne počne nova runda.",
                "Daj iskren kompliment osobi sa tvoje leve strane.",
                "Postavi smešan status i posalji u grupu.",
                "Daj pobednicima da krekuju.",
                "Oil up lil bro."
            )
        }
        return list.random()
    }
}
