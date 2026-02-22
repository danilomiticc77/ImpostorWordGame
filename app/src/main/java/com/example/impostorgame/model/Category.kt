package com.example.impostorgame.model

/**
 * Represents a game category with words in both English and Serbian.
 * @param id Unique identifier for the category
 * @param nameEn English display name
 * @param nameSr Serbian display name
 * @param wordsEn List of word items in English
 * @param wordsSr List of word items in Serbian
 */
data class Category(
    val id: String,
    val nameEn: String,
    val nameSr: String,
    val wordsEn: List<WordItem>,
    val wordsSr: List<WordItem>
) {
    /** Returns the localized name based on language */
    fun getName(language: Language): String = when (language) {
        Language.EN -> nameEn
        Language.SR -> nameSr
    }

    /** Returns the localized word list based on language */
    fun getWords(language: Language): List<WordItem> = when (language) {
        Language.EN -> wordsEn
        Language.SR -> wordsSr
    }
}
