package com.example.hotpot.data.auth.login


interface LoginRepository {
    suspend fun login(request: LoginRequest) : LoginResult
}
sealed class LoginResult {
    data class Success(val accessToken : String) : LoginResult()
    data class Error(val code: Int, val message: String?) : LoginResult()
}