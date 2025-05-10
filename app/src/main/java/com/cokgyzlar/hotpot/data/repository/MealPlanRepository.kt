package com.cokgyzlar.hotpot.data.repository

import com.cokgyzlar.hotpot.data.model.Diet
import com.cokgyzlar.hotpot.data.model.MealPlan
import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.data.model.UserGoal


object MealPlanRepository {

    private val mealPlan = MealPlan(
        userGoal = UserGoal(
            userId = 1,
            calorieGoal = 1000,
            proteinGoal = 100,
            carbohydrateGoal = 100,
            fatGoal = 100,
            dietType = "Balanced",
            weightGoal = 70.0,
            dateCreated = "2025-05-11"
        ),
        diet = Diet(
            dietName = "Balanced",
            dietDescription = "A balanced diet for health and fitness",
            isActive = true,
            startDate = "2025-05-11"
        )
    )

    private var userGoal: UserGoal = UserGoal(
        userId = 1,
        calorieGoal = 1000,
        proteinGoal = 100,
        carbohydrateGoal = 100,
        fatGoal = 100,
        dietType = "Balanced",
        weightGoal = 70.0,
        dateCreated = "2025-05-11"
    )

    // Fetch the user's goal
    fun getUserGoal(): UserGoal = userGoal

    // Update the user's goal
    fun updateUserGoal(updatedGoal: UserGoal) {
        userGoal = updatedGoal
    }

    // Fetch a recipe by ID (delegated to RecipeRepositoryLocal)
    fun getRecipeById(recipeId: Int): Recipe? {
        return RecipeRepositoryLocal.getRecipeById(recipeId)
    }

    // Add a recipe to the meal plan if it fits the user's goals
    fun addRecipeToPlan(recipe: Recipe): MealPlan {
        if (isRecipeCompatibleWithGoals(recipe)) {
            mealPlan.addRecipe(recipe)
        }
        return mealPlan
    }

    // Remove a recipe from the meal plan
    fun removeRecipeFromPlan(recipeId: Int): MealPlan {
        mealPlan.removeRecipe(recipeId)
        return mealPlan
    }

    // Check if a recipe is compatible with the user's diet and goals
    private fun isRecipeCompatibleWithGoals(recipe: Recipe): Boolean {
        val isCalorieCompatible = recipe.calories.total <= userGoal.calorieGoal
        val isProteinCompatible = recipe.calories.protein >= userGoal.proteinGoal
        val isCarbCompatible = recipe.calories.carbs <= userGoal.carbohydrateGoal
        val isFatCompatible = recipe.calories.fats <= userGoal.fatGoal

        // Check if the recipe type aligns with the user's diet type (if any)
        val isDietTypeCompatible = userGoal.dietType?.let { it == recipe.mealType.name } ?: true

        return isCalorieCompatible && isProteinCompatible && isCarbCompatible && isFatCompatible && isDietTypeCompatible
    }
}
