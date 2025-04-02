package com.example.hotpot.data.auth.register

interface RegisterRepository {
    suspend fun register(request: RegisterRequest) : RegisterResult
}
sealed class RegisterResult {
    data class Success(val status: String) : RegisterResult()
    data class Error(val code: Int, val message: String?) : RegisterResult()
}