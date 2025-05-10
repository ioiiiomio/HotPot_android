package com.cokgyzlar.hotpot.data.model

data class Meal(
    val id: Int,
    val type: String,
    val title: String,
    val calories: Calories
)


data class DailyMeal(
    val user_id: Int,
    val date: String,
    val calories: Calories,
    val calorie_normal: Calories,
    val breakfast: MealDetail,
    val lunch: MealDetail,
    val dinner: MealDetail,
    val snacks: MealDetail,
    val water_normal: Int,
    val water_total: Int
)

data class MealDetail(
    val calorie_total: Int,
    val meals: List<Meal>
)

data class CalorieNorm(
    val user_id : Int,
    val calorie_normal : Calories,
    val water_normal : Int
)


