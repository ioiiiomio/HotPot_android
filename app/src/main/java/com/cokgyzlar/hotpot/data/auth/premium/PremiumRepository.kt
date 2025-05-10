package com.cokgyzlar.hotpot.data.auth.premium

interface PremiumRepository {
    suspend fun upgrade() : Result
    suspend fun isPremium() : Boolean
}
sealed class Result {
    data class Success(val status: String) : Result()
    data class Error(val code: Int, val message: String?) : Result()
}