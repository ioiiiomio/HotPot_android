package com.cokgyzlar.hotpot.data.model
import com.cokgyzlar.hotpot.models.Calories

data class Recipe(
    val id: Int,
    val author_id: Int,
    val name: String,
    val description: String,
    val calories: Calories,
    val imageUrl: String,
    val meal_type: String,
    val ingredients: List<String>,
    val instructions: List<String>,
    val is_favorite: Boolean?,
    val average_rating : Float,
    val ratings_count : Float
)
