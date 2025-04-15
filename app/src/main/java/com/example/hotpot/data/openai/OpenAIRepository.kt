package com.example.hotpot.data.openai

interface OpenAIRepository {
    suspend fun getChatResponse(token: String, request: ChatRequest): OpenAIResult
}
sealed class OpenAIResult {
    data class Success(val chatResponse: ChatResponse) : OpenAIResult()
    data class Error(val code: Int, val message: String?) : OpenAIResult()
}