package com.cokgyzlar.hotpot.data.model

// User Goal represents the nutritional and weight-related goals of the user
data class UserGoal(
    val userId: Int,                    // User ID related to the goal
    val calorieGoal: Int,               // Target calories per day
    val proteinGoal: Int,               // Protein goal (grams)
    val carbohydrateGoal: Int,          // Carbohydrate goal (grams)
    val fatGoal: Int,                   // Fat goal (grams)
    val dietType: String? = null,       // Optional: e.g., "Keto", "Vegan", etc.
    val weightGoal: Double? = null,     // Optional: target weight (kg)
    val dateCreated: String             // Date the goal was created
)

// Diet class represents the diet plan (such as Keto, Vegan, etc.)
data class Diet(
    val dietName: String,               // Name of the diet (e.g., "Keto", "Vegan")
    val dietDescription: String?,       // Optional description about the diet
    val isActive: Boolean = true,       // Flag to indicate if the diet plan is currently active
    val startDate: String,              // Start date of the diet
    val endDate: String? = null         // Optional end date for the diet plan
)

// MealPlan class to manage the user's meals, ensuring it aligns with their goals
class MealPlan(
    val userGoal: UserGoal,            // The user's goal (Calories, Protein, Carbs, Fats, etc.)
    val diet: Diet,                    // The diet type user is following
    val recipes: MutableList<Recipe> = mutableListOf()  // List of recipes in the meal plan
) {
    // Adds a recipe to the meal plan
    fun addRecipe(recipe: Recipe): Boolean {
        // Check if the recipe's nutritional values are within the user's goals
        return if (recipe.calories.total <= userGoal.calorieGoal &&
            recipe.calories.protein <= userGoal.proteinGoal &&
            recipe.calories.carbs <= userGoal.carbohydrateGoal &&
            recipe.calories.fats <= userGoal.fatGoal) {
            recipes.add(recipe)
            true
        } else {
            false
        }
    }

    // Removes a recipe from the meal plan
    fun removeRecipe(recipeId: Int): Boolean {
        val recipeToRemove = recipes.find { it.id == recipeId }
        return if (recipeToRemove != null) {
            recipes.remove(recipeToRemove)
            true
        } else {
            false
        }
    }

    // Retrieves the total calories for the entire meal plan
    fun getTotalCalories(): Int {
        return recipes.sumBy { it.calories.total }
    }

    // Retrieves the total protein for the entire meal plan
    fun getTotalProtein(): Int {
        return recipes.sumBy { it.calories.protein }
    }

    // Retrieves the total carbs for the entire meal plan
    fun getTotalCarbs(): Int {
        return recipes.sumBy { it.calories.carbs }
    }

    // Retrieves the total fats for the entire meal plan
    fun getTotalFats(): Int {
        return recipes.sumBy { it.calories.fats }
    }
}
