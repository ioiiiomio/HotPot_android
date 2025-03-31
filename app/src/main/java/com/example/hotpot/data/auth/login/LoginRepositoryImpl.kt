package com.example.hotpot.data.auth.login

import android.util.Log
import retrofit2.HttpException

class LoginRepositoryImpl(
    private val api : LoginApi
) : LoginRepository {
    override suspend fun login(request: LoginRequest): LoginResult {
        return try{
            val response = api.login(request)
            Log.e("Repository", "success")
            LoginResult.Success(response.data.access_token)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            LoginResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            LoginResult.Error(500, e.message)
        }
    }
}