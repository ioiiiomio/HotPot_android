package com.example.hotpot.data.auth.login

import com.example.hotpot.data.NoAuth
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {
    @NoAuth
    @POST("/auth/api/v1/login")
    suspend fun login(@Body request: LoginRequest) : LoginResponse
}

data class LoginRequest(
    val email : String,
    val password: String
)

data class LoginResponse(
    val code : Int,
    val data : LoginResponseData,
    val message : String
)

data class LoginResponseData(
    val access_token : String
)