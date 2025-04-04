package com.example.hotpot.data

import android.util.Base64
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.time.Duration
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

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

    fun getRelativeTime(timestamp: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")

        return try {
            val past = sdf.parse(timestamp)
            val now = Date()
            val diff = now.time - past.time

            val minutes = TimeUnit.MILLISECONDS.toMinutes(diff)
            val hours = TimeUnit.MILLISECONDS.toHours(diff)
            val days = TimeUnit.MILLISECONDS.toDays(diff)

            when {
                minutes < 1 -> "just now"
                minutes < 60 -> "$minutes min ago"
                hours < 24 -> "$hours h ago"
                days < 7 -> "$days d ago"
                else -> {
                    val weeks = days / 7
                    "$weeks w ago"
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            "unknown"
        }
    }


}