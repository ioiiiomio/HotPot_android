package com.example.hotpot.data.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotpot.data.model.MealType
import com.example.hotpot.data.model.Recipe
import com.example.hotpot.data.repository.RecipeRepository

data class MealTypeWithRecipes(
    val mealType: MealType,
    val recipes: List<Recipe>
)

class RecipeViewModel : ViewModel() {

    private val _mealTypesWithRecipes = MutableLiveData<List<MealTypeWithRecipes>>()
    val mealTypesWithRecipes: LiveData<List<MealTypeWithRecipes>> get() = _mealTypesWithRecipes

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        // Fetch recipes from the repository
        val recipes = RecipeRepository.getAllRecipes()

        // Group the recipes by MealType
        val groupedRecipes = MealType.values().map { mealType ->
            MealTypeWithRecipes(
                mealType,
                RecipeRepository.getRecipesByMealType(mealType)
            )
        }

        // Update LiveData with the grouped recipes
        _mealTypesWithRecipes.value = groupedRecipes
    }
}
