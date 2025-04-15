package com.example.hotpot.ui.viewmodels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotpot.data.meal.MealRepository
import com.example.hotpot.data.meal.MealResult
import com.example.hotpot.data.openai.ChatRequest
import com.example.hotpot.data.openai.Message
import com.example.hotpot.data.openai.OpenAIRepository
import com.example.hotpot.data.openai.OpenAIResult
import com.example.hotpot.data.profile.ProfileRepository
import com.example.hotpot.data.profile.UserResult
import com.example.hotpot.models.CalorieNorm
import com.example.hotpot.models.Calories
import com.example.hotpot.models.DailyMeal
import com.example.hotpot.models.Meal
import com.example.hotpot.models.MealDetail
import com.example.hotpot.models.UserProfile
import com.google.gson.Gson
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class MainActivityVM : ViewModel() {
    private val profileRepository: ProfileRepository by lazy { getKoin().get<ProfileRepository>() }
    private val openAIRepository: OpenAIRepository by lazy { getKoin().get<OpenAIRepository>() }
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}
    private val mealRepository: MealRepository by lazy { getKoin().get<MealRepository>() }
    var calorieNorm = MutableLiveData<CalorieNorm?>()
    var dailyMeal = MutableLiveData<DailyMeal>()
    val healthLevel = MutableLiveData<Feedback>()
    private var currentDate: String? = null

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

        viewModelScope.launch {
            val postResult = mealRepository.postMeal(newDailyMeal)

            if (postResult is MealResult.Success) {
                Log.e("MainActivityVM", "New meal posted successfully.")
            } else {
                Log.e(
                    "MainActivityVM",
                    "Failed to post new meal: ${(postResult as MealResult.Error).message}"
                )
            }
        }

    }

    fun addWaterIntake(){
        val currentDailyMeal = dailyMeal.value
        val newDailyMeal = currentDailyMeal!!.copy(water_total = currentDailyMeal.water_total + 250)
        appStorage.saveDailyMeal(newDailyMeal)
        dailyMeal.postValue(newDailyMeal)
        viewModelScope.launch {
            val postResult = mealRepository.postMeal(newDailyMeal)

            if (postResult is MealResult.Success) {
                Log.e("MainActivityVM", "New meal posted successfully.")
            } else {
                Log.e(
                    "MainActivityVM",
                    "Failed to post new meal: ${(postResult as MealResult.Error).message}"
                )
            }
        }
    }


    fun initializeDailyMeal(norm: CalorieNorm) {
        viewModelScope.launch {
            val today = getCurrentDate()

            val result = mealRepository.getMeal(today)

            if (result is MealResult.Success && result.meal != null) {
                appStorage.saveDailyMeal(result.meal)
                dailyMeal.postValue(result.meal!!)
                Log.e("abcd", result.meal.toString())
            } else {
                val newMeal = DailyMeal(
                    user_id = appStorage.getId() ?: 1,
                    date = today,
                    calories = Calories(0, 0, 0, 0),
                    calorie_normal = norm.calorie_normal,
                    breakfast = MealDetail(0, listOf()),
                    lunch = MealDetail(0, listOf()),
                    dinner = MealDetail(0, listOf()),
                    snacks = MealDetail(0, listOf()),
                    water_normal = norm.water_normal,
                    water_total = 0
                )

                appStorage.saveDailyMeal(newMeal)
                dailyMeal.postValue(newMeal)

                val postResult = mealRepository.postMeal(newMeal)
                if (postResult is MealResult.Success) {
                    Log.e("MainActivityVM", "New meal posted successfully.")
                } else {
                    Log.e("MainActivityVM", "Failed to post new meal: ${(postResult as MealResult.Error).message}")
                }
            }
        }
    }

    fun fetchOrInitializeDailyMealForDate(date: String, norm: CalorieNorm) {
        viewModelScope.launch {
            val result = mealRepository.getMeal(date)

            if (result is MealResult.Success && result.meal != null) {
                appStorage.saveDailyMeal(result.meal)
                dailyMeal.postValue(result.meal!!)
            } else {
                val newMeal = DailyMeal(
                    user_id = appStorage.getId() ?: 1,
                    date = date,
                    calories = Calories(0, 0, 0, 0),
                    calorie_normal = norm.calorie_normal,
                    breakfast = MealDetail(0, listOf()),
                    lunch = MealDetail(0, listOf()),
                    dinner = MealDetail(0, listOf()),
                    snacks = MealDetail(0, listOf()),
                    water_normal = norm.water_normal,
                    water_total = 0
                )
                appStorage.saveDailyMeal(newMeal)
                dailyMeal.postValue(newMeal)
                mealRepository.postMeal(newMeal)
            }
        }
    }



    fun getTodayDate(): String {
        val formatter = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        return formatter.format(Date())
    }

    fun getCurrentDate(): String {
        if (currentDate == null) {
            currentDate = getTodayDate()
            Log.e("abcd", currentDate!!)
        }
        return currentDate!!
    }

    fun nextDate() {
        val date = SimpleDateFormat("dd-MM-yyyy").parse(getCurrentDate())
        val calendar = Calendar.getInstance().apply { time = date }
        calendar.add(Calendar.DATE, 1)
        currentDate = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
    }

    fun previousDate() {
        val date = SimpleDateFormat("dd-MM-yyyy").parse(getCurrentDate())
        val calendar = Calendar.getInstance().apply { time = date }
        calendar.add(Calendar.DATE, -1)
        currentDate = SimpleDateFormat("dd-MM-yyyy").format(calendar.time)
    }

    private fun getLast7DaysDates(): List<String> {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val calendar = Calendar.getInstance()
        return (0..6).map {
            val date = dateFormat.format(calendar.time)
            calendar.add(Calendar.DAY_OF_YEAR, -1)
            date
        }
    }

    suspend fun collectDataAndSendPrompt(token: String, id: Int) {
        val feedback = appStorage.getFeedback()
        if(feedback!=null && feedback.date==getTodayDate()){
            healthLevel.postValue(feedback!!)
            return
        }
        try {
            val userResult = profileRepository.getUser(id)
            if(userResult is UserResult.Success){
                val userProfile = userResult.user
                val dates = getLast7DaysDates()
                val meals = dates.mapNotNull { date ->
                    when (val result = mealRepository.getMeal(date)) {
                        is MealResult.Success -> result.meal
                        is MealResult.Error -> null
                    }
                }
                val prompt = buildPrompt(userProfile, meals)
                sendPromptToOpenAI(token, prompt)
            }

        } catch (e: Exception) {
            Log.e("ProgressFragment", "Error: ${e.message}")
        }
    }

    private fun buildPrompt(profile: UserProfile, meals: List<DailyMeal>): String {
        val profileJson: String = Gson().toJson(profile)
        val mealsJson: String = Gson().toJson(meals)
        return """
        Based on my profile:
        ${profileJson.toString()}
        
        And the data from the last 7 days about consumed meals:
        ${mealsJson.toString()}
        
        Please rate my health level from 1 to 100 and provide feedback in this json, return json only, make feedback no longer than 30 words:
        { rating: 1, feedback: "some feedback" }
    """.trimIndent()
    }


    private suspend fun sendPromptToOpenAI(token: String, prompt: String) {
        val request = ChatRequest(
            model = "gpt-3.5-turbo",
            messages = listOf(
                Message("user", Gson().toJson(prompt))
            )
        )
        Log.e("abcd", Gson().toJson(prompt))

        val result = openAIRepository.getChatResponse("Bearer $token", request)

        when (result) {
            is OpenAIResult.Success -> {
                val content = result.chatResponse.choices.firstOrNull()?.message?.content
                val feedback = Gson().fromJson(content, Feedback::class.java)
                feedback.date=getTodayDate()
                appStorage.saveFeedback(feedback)
                healthLevel.postValue(feedback)
                Log.d("OpenAI", "Response: $content")
            }
            is OpenAIResult.Error -> {
                Log.e("OpenAI", "Error: ${result.message}")
            }
        }
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
    data class Feedback(
        val rating : Int,
        val feedback : String,
        var date : String
    )



}