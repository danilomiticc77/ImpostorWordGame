package com.example.impostorgame.data

import android.content.Context
import com.example.impostorgame.model.Category
import com.example.impostorgame.model.WordItem
import org.json.JSONArray
import org.json.JSONObject

object CustomCategoryManager {
    private const val PREFS_NAME = "impostor_custom_categories"
    private const val KEY_CATEGORIES = "categories"

    fun getCustomCategories(context: Context): List<Category> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val jsonString = prefs.getString(KEY_CATEGORIES, null) ?: return emptyList()

        return try {
            val jsonArray = JSONArray(jsonString)
            val categories = mutableListOf<Category>()
            for (i in 0 until jsonArray.length()) {
                val obj = jsonArray.getJSONObject(i)
                val id = obj.getString("id")
                val nameEn = obj.getString("nameEn")
                val nameSr = obj.getString("nameSr")
                
                val wordsArray = obj.getJSONArray("words")
                val words = mutableListOf<WordItem>()
                for (j in 0 until wordsArray.length()) {
                    words.add(WordItem(wordsArray.getString(j), ""))
                }

                categories.add(
                    Category(
                        id = id,
                        nameEn = nameEn,
                        nameSr = nameSr,
                        wordsEn = words,
                        wordsSr = words // Custom categories use the same words for both languages for simplicity
                    )
                )
            }
            categories
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun saveCustomCategory(context: Context, category: Category) {
        val currentCategories = getCustomCategories(context).toMutableList()
        
        // Replace if exists, otherwise add
        val existingIndex = currentCategories.indexOfFirst { it.id == category.id }
        if (existingIndex >= 0) {
            currentCategories[existingIndex] = category
        } else {
            currentCategories.add(category)
        }

        saveAll(context, currentCategories)
    }

    fun deleteCustomCategory(context: Context, categoryId: String) {
        val currentCategories = getCustomCategories(context).toMutableList()
        currentCategories.removeAll { it.id == categoryId }
        saveAll(context, currentCategories)
    }

    private fun saveAll(context: Context, categories: List<Category>) {
        val jsonArray = JSONArray()
        for (cat in categories) {
            val obj = JSONObject()
            obj.put("id", cat.id)
            obj.put("nameEn", cat.nameEn)
            obj.put("nameSr", cat.nameSr)

            val wordsArray = JSONArray()
            // We only need to save one list since they are identical for custom ones
            for (word in cat.wordsEn) {
                wordsArray.put(word.word)
            }
            obj.put("words", wordsArray)

            jsonArray.put(obj)
        }

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CATEGORIES, jsonArray.toString()).apply()
    }
}
