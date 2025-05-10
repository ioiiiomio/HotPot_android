package com.cokgyzlar.hotpot.data.auth.premium

import android.util.Log
import retrofit2.HttpException

class PremiumRepositoryImpl (
    private val api : PremiumApi
) : PremiumRepository{
    override suspend fun upgrade(): Result {
        return try{
            val response = api.upgrade()
            Log.e("Repository", "success")
            Result.Success("success")
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            Result.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            Result.Error(500, e.message)
        }
    }

    override suspend fun isPremium(): Boolean {
        return try{
            val response = api.isPremium()
            Log.e("Repository", "success")
            true
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            false
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            false
        }
    }
}