package com.cokgyzlar.hotpot.data.meal

import com.cokgyzlar.hotpot.models.DailyMeal

interface MealRepository {
    suspend fun getMeal(date: String) : MealResult
    suspend fun postMeal(dailyMeal: DailyMeal) : MealResult
}
sealed class MealResult {
    data class Success(val meal: DailyMeal?) : MealResult()
    data class Error(val code: Int, val message: String?) : MealResult()
}