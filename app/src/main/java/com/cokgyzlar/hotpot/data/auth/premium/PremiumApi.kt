package com.cokgyzlar.hotpot.data.auth.premium

import com.cokgyzlar.hotpot.data.NoAuth
import com.cokgyzlar.hotpot.data.RequiresAuth
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET

interface PremiumApi {
    @RequiresAuth
    @POST("/user/api/v1/users/upgrade/premium")
    suspend fun upgrade() : Response

    @RequiresAuth
    @GET("/user/api/v1/users/premium/status")
    suspend fun isPremium() : Status
}


data class Response(
    val code : Int,
    val data : Data?,
    val message : String
)

data class Status(
    val date: String,
    val exp_date: String,
    val user_id: Int
)

data class Data(
    val message: String
)