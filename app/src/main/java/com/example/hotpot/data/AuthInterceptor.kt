package com.example.hotpot.data

import android.util.Base64
import android.util.Log
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

class AuthInterceptor(val appStorage: AppStorage): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        val requireAuth = requiresAuthentication(originalRequest)

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

    private fun getAccessToken() : String?{
        var accessToken = appStorage.getAccessToken()
        return accessToken
    }

    private fun isJwtTokenExpired(token: String): Boolean {
        try {
            val parts = token.split(".")
            if (parts.size != 3) throw IllegalArgumentException("Invalid JWT token")

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val payloadJson = JSONObject(payload)

            if (!payloadJson.has("exp")) throw IllegalArgumentException("No expiration claim in token")

            val expirationTime = payloadJson.getLong("exp") * 1000
            return Date().time > expirationTime
        } catch (e: Exception) {
            e.printStackTrace()
            return true
        }
    }
}