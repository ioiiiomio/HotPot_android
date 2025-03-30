package com.example.hotpot.data.auth.register

import android.util.Log
import retrofit2.HttpException

class RegisterRepositoryImpl (
    private val api : RegisterApi
) : RegisterRepository{
    override suspend fun register(request: RegisterRequest): RegisterResult {
        return try{
            val response = api.register(request)
            Log.e("Repository", "success")
            RegisterResult.Success("success")
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            RegisterResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            RegisterResult.Error(500, e.message)
        }
    }
}