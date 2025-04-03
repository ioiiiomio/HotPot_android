package com.example.hotpot.data

import android.util.Base64
import android.util.Log
import com.example.hotpot.data.Utils.isJwtTokenExpired
import com.example.hotpot.data.auth.login.LoginRepository
import com.example.hotpot.data.auth.login.LoginRequest
import com.example.hotpot.data.auth.login.LoginResult
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject
import retrofit2.Invocation
import java.lang.reflect.Method
import java.util.Date

class AuthInterceptor(val appStorage: AppStorage, val loginRepository: LoginRepository): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requireAuth = requiresAuthentication(originalRequest)

        Log.e("auth is required?", requireAuth.toString())

        val requestBuilder: Request.Builder = originalRequest.newBuilder().apply {
            header("User-Agent", "android")
            if (requireAuth) {
                val token = runBlocking { getAccessToken() }
                Log.e("auth is required", token.toString())
                token?.let {
                    addHeader("Authorization", it)
                }
            }
        }

        val modifiedRequest = requestBuilder.build()
        return chain.proceed(modifiedRequest)
    }

    private fun requiresAuthentication(request: Request): Boolean {
        val method: Method? = request.tag(Invocation::class.java)?.method()
        return method?.isAnnotationPresent(RequiresAuth::class.java) == true
    }

    private suspend fun getAccessToken() : String?{
        var accessToken = appStorage.getAccessToken()

        if (accessToken==null || isJwtTokenExpired(accessToken)) {
            try {
                val result = loginRepository.login(LoginRequest(appStorage.getEmail()!!, appStorage.getPassword()!!))
                if(result is LoginResult.Success){
                    appStorage.saveAccessToken(result.accessToken)
                    return result.accessToken
                }
            } catch (e: Exception) {
                Log.e("AuthInterceptor", "Failed to fetch token: ${e.message}")
                accessToken=null
            }
        }
        return accessToken
    }
}