package com.cokgyzlar.hotpot.data.openai

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAIApi {
        @POST("chat/completions")
        suspend fun getChatResponse(@Header("Authorization") token: String, @Body request: ChatRequest): ChatResponse
}
data class ChatRequest(
    val model: String,
    val messages: List<Message>
)

data class Message(
    val role: String,
    val content: String
)
data class ChatResponse(
    val id: String,
    val choices: List<Choice>
)

data class Choice(
    val message: Message
)
