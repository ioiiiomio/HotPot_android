package com.cokgyzlar.hotpot.data.api

import com.cokgyzlar.hotpot.data.model.Recipe
import retrofit2.http.GET
import retrofit2.http.Path

interface RecipeApi {
    @GET("recipes")
    suspend fun getRecipes(): List<Recipe>

    @GET("recipes/{id}")
    suspend fun getRecipeById(@Path("id") recipeId: Int): Recipe
}
