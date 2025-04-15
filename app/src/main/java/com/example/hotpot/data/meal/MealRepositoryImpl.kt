package com.example.hotpot.data.meal

import android.util.Log
import com.example.hotpot.models.DailyMeal
import retrofit2.HttpException

class MealRepositoryImpl (
    private val api : MealApi
) : MealRepository{
    override suspend fun getMeal(date: String): MealResult {
        return try{
            val response = api.getDailyMeal(date)
            Log.e("Repository", "success")
            MealResult.Success(response.data)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            MealResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            MealResult.Error(500, e.message)
        }
    }

    override suspend fun postMeal(dailyMeal: DailyMeal): MealResult {
        return try{
            val response = api.postMeal(dailyMeal)
            Log.e("Repository", "success")
            MealResult.Success(null)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            MealResult.Error(e.code(), e.message())
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            MealResult.Error(500, e.message)
        }
    }
}