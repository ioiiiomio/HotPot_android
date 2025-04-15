package com.example.hotpot.data.auth.register

import com.example.hotpot.data.NoAuth
import retrofit2.http.POST
import retrofit2.http.Body

interface RegisterApi {
    @NoAuth
    @POST("/auth/api/v1/register")
    suspend fun register(@Body request: RegisterRequest) : RegisterResponse
}

data class RegisterRequest(
    val birth_date: String,
    val email: String,
    val firstname: String,
    val lastname: String,
    val password: String,
    val profile_picture: String,
    val sex: String,
    val username: String,
    val vision: List<String>
)


data class RegisterResponse(
    val code : Int,
    val data : RegisterResponseData?,
    val message : String
)

data class RegisterResponseData(
    val message: String
)