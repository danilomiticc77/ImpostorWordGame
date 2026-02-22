package com.example.impostorgame.data

import com.example.impostorgame.data.categories.*
import com.example.impostorgame.model.Category

/**
 * Repository that provides all available game categories.
 * Single source of truth for category data.
 */
object CategoryRepository {

    /** All available categories in the game */
    val allCategories: List<Category> = listOf(
        animalsCategory(),
        foodDrinkCategory(),
        countriesCitiesCategory(),
        everydayObjectsCategory(),
        famousPeopleCategory(),
        brandsLogosCategory(),
        colorsShapesCategory(),
        emotionsFeelingsCategory(),
        hobbiesActivitiesCategory(),
        internetCultureCategory(),
        cookingKitchenCategory(),
        moviesTvCategory(),
        musicBandsCategory(),
        occupationsCategory(),
        schoolEducationCategory(),
        scienceTechCategory(),
        sportsCategory(),
        superheroesCategory(),
        transportCategory(),
        videoGamesCategory(),
        weatherNatureCategory()
    )

    /** Look up a category by its ID */
    fun getCategoryById(id: String): Category? =
        allCategories.find { it.id == id }
}
