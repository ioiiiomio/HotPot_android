package com.cokgyzlar.hotpot.data.openai

import android.util.Log
import retrofit2.HttpException

class OpenAIRepositoryImpl (
    private val api : OpenAIApi
) : OpenAIRepository{

    override suspend fun getChatResponse(token: String, request: ChatRequest): OpenAIResult {
        return try{
            val response = api.getChatResponse(token, request)
            Log.e("Repository", response.toString())
            OpenAIResult.Success(response)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            OpenAIResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            OpenAIResult.Error(500, e.message)
        }
    }
}