package com.cokgyzlar.hotpot.data.meal

import com.cokgyzlar.hotpot.data.model.Recipe
import com.cokgyzlar.hotpot.models.DailyMeal

interface MealRepository {
    suspend fun getMeal(date: String) : MealResult
    suspend fun postMeal(dailyMeal: DailyMeal) : MealResult
    suspend fun getRecipes() : RecipesResult
    suspend fun postRating(id: Int, score: Int) : Result
}
sealed class MealResult {
    data class Success(val meal: DailyMeal?) : MealResult()
    data class Error(val code: Int, val message: String?) : MealResult()
}
sealed class RecipesResult {
    data class Success(val recipes: List<Recipe>) : RecipesResult()
    data class Error(val code: Int, val message: String?) : RecipesResult()
}
sealed class Result {
    data class Success(val status: String) : Result()
    data class Error(val code: Int, val message: String?) : Result()
}