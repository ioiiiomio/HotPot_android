package com.example.hotpot.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.18/api/v1/recipes"  // Update with your local API

    val instance: RecipeApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Converts JSON to Kotlin objects
            .build()
            .create(RecipeApi::class.java)
    }
}

