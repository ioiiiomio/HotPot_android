package com.example.hotpot.data.model

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val calories: Int,
    val imageUrl: String,
    val mealType: MealType,
    val ingredients: List<String>,
    val instructions: List<String>,
    val isFavorite: Boolean?
)
