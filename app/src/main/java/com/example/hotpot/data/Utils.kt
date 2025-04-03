package com.example.hotpot.data

import android.util.Base64
import org.json.JSONObject
import java.util.Date

object Utils {
    fun isJwtTokenExpired(token: String): Boolean {
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
    fun getRole(token: String): String? {
        try {
            val parts = token.split(".")
            if (parts.size != 3) throw IllegalArgumentException("Invalid JWT token")

            val payload = String(Base64.decode(parts[1], Base64.URL_SAFE))
            val payloadJson = JSONObject(payload)

            if (!payloadJson.has("role")) throw IllegalArgumentException("No role data in token")

            val role = payloadJson.getInt("role")
            if(role==1){
                return "user"
            }else if(role==2){
                return "dietologist"
            }else if(role==3){
                return "admin"
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
        return null
    }
}