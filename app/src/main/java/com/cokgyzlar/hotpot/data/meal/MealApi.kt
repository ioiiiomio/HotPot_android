package com.cokgyzlar.hotpot.data.meal

import com.cokgyzlar.hotpot.data.RequiresAuth
import com.cokgyzlar.hotpot.data.posts.comments.Response
import com.cokgyzlar.hotpot.models.DailyMeal
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Path

interface MealApi {
    @RequiresAuth
    @GET("meal/api/v1/{date}")
    suspend fun getDailyMeal(@Path("date") date: String): DailyMealResponse

    @RequiresAuth
    @POST("meal/api/v1")
    suspend fun postMeal(@Body request: DailyMeal): Response
}

data class Response(
    val code : Int,
    val data : MealResponseData?,
    val message : String
)
data class DailyMealResponse(
    val code : Int,
    val data : DailyMeal,
    val message : String
)

data class MealResponseData(
    val message: String
)