package com.example.hotpot.ui.viewmodels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotpot.data.model.MealType
import com.example.hotpot.models.CalorieNorm
import com.example.hotpot.models.Calories
import com.example.hotpot.models.DailyMeal
import com.example.hotpot.models.Meal
import com.example.hotpot.models.MealDetail
import com.prowheelxrassistv01.data.AppStorage
import org.koin.mp.KoinPlatform.getKoin
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivityVM : ViewModel() {
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}
    var calorieNorm = MutableLiveData<CalorieNorm?>()
    var dailyMeal = MutableLiveData<DailyMeal>()

    fun fetchOrInitializeCalorieNorm() {
        val cachedNorm = appStorage.getCalorieNorm()

        if (cachedNorm == null) {
            calorieNorm.postValue(null)
        } else {
            calorieNorm.postValue(cachedNorm)
        }
    }

    fun updateCalorieNorm(norm: CalorieNorm) {
        Log.e("abcd", norm.toString() )
        appStorage.saveCalorieNorm(norm)
        calorieNorm.postValue(norm)
    }

    fun updateDailyMeal(mealType: String, meals: List<Meal>){
        val newDailyMeal = recalculate(mealType, meals, dailyMeal.value!!)
        appStorage.saveDailyMeal(newDailyMeal)
        dailyMeal.postValue(newDailyMeal)
    }

    fun addWaterIntake(){
        val currentDailyMeal = dailyMeal.value
        val newDailyMeal = currentDailyMeal!!.copy(water_total = currentDailyMeal.water_total + 250)
        appStorage.saveDailyMeal(newDailyMeal)
        dailyMeal.postValue(newDailyMeal)
    }

    fun initializeDailyMeal(norm : CalorieNorm) {
        val savedMeal = appStorage.getDailyMeal()
        if(savedMeal==null || savedMeal.date!=getTodayDate()){
            dailyMeal.postValue(
                DailyMeal(
                    user_id = appStorage.getId() ?: 1,
                    date = getTodayDate(),
                    calories = Calories(0, 0, 0, 0),
                    calorie_normal = norm.calorie_normal,
                    breakfast = MealDetail(0, listOf()),
                    lunch = MealDetail(0, listOf()),
                    dinner = MealDetail(0, listOf()),
                    snacks = MealDetail(0, listOf()),
                    water_normal = norm.water_normal,
                    water_total = 0
                )
            )
        }else{
            dailyMeal.postValue(savedMeal!!)
        }
    }

    fun getTodayDate(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    fun recalculate(
        mealType: String,
        newMeals: List<Meal>,
        dailyMeal: DailyMeal
    ): DailyMeal {
        val updatedMealDetail = MealDetail(
            calorie_total = newMeals.sumOf { it.calories.total },
            meals = newMeals
        )

        val updatedDailyMeal = when (mealType.lowercase()) {
            "breakfast" -> dailyMeal.copy(breakfast = updatedMealDetail)
            "lunch" -> dailyMeal.copy(lunch = updatedMealDetail)
            "dinner" -> dailyMeal.copy(dinner = updatedMealDetail)
            "snacks" -> dailyMeal.copy(snacks = updatedMealDetail)
            else -> dailyMeal
        }

        val allMeals = updatedDailyMeal.breakfast.meals +
                updatedDailyMeal.lunch.meals +
                updatedDailyMeal.dinner.meals +
                updatedDailyMeal.snacks.meals

        val totalCalories = allMeals.sumOf { it.calories.total }
        val totalProtein = allMeals.sumOf { it.calories.protein }
        val totalFats    = allMeals.sumOf { it.calories.fats }
        val totalCarbs   = allMeals.sumOf { it.calories.carbs }

        return updatedDailyMeal.copy(
            calories = Calories(
                total = totalCalories,
                protein = totalProtein,
                fats = totalFats,
                carbs = totalCarbs
            )
        )
    }

}