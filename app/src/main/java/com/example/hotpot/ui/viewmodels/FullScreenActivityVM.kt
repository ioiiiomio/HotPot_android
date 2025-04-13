package com.example.hotpot.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hotpot.models.CalorieNorm
import com.example.hotpot.models.Calories
import com.example.hotpot.models.Dietician
import com.example.hotpot.models.HealthDetail
import com.example.hotpot.models.PostItem
import com.example.hotpot.models.UserProfile
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

class FullScreenActivityVM : ViewModel() {
    var userProfile = MutableLiveData<UserProfile>()
    var dieticianProfile = MutableLiveData<Dietician>()
    var posts = MutableLiveData<List<PostItem>>()
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}
    private val mainActivityVM: MainActivityVM by lazy { getKoin().get<MainActivityVM>()}


    fun updateHealthDetails(height: Int, weight: Double, dob: String, sex: String) {
        val currentProfile = userProfile.value ?: return

        val newHealthDetail = HealthDetail(
            height = height,
            weight = weight,
            created_at = Timestamp(System.currentTimeMillis()).toString()
        )

        val updatedProfile = currentProfile.copy(
            birth_date = dob,
            sex = sex,
            health_details = currentProfile.health_details + newHealthDetail
        )

        userProfile.value = updatedProfile
        viewModelScope.launch {
            generateAndUpdateCalorieNorm()
        }
    }

    fun updateVisions(visions : List<String>){
        val currentProfile = userProfile.value ?: return
        val updatedProfile = currentProfile.copy(
            vision = visions
        )
        userProfile.value=updatedProfile
        viewModelScope.launch {
            generateAndUpdateCalorieNorm()
        }
    }

    suspend fun generateAndUpdateCalorieNorm() {
//        val profile = userProfile.value ?: return
//
//        val prompt = "Based on a ${profile.sex} born on ${profile.birth_date} weighing ${
//            profile.health_details.lastOrNull()?.weight ?: "unknown"
//        } kg and height ${
//            profile.health_details.lastOrNull()?.height ?: "unknown"
//        } cm, provide a recommended daily calorie, protein, fat, and carb intake in JSON format like {\"total\":2300, \"protein\":120, \"fats\":70, \"carbs\":300}."
//
//        val request = OpenAiRequest(
//            messages = listOf(Message("user", prompt))
//        )
//
//        val response = openAiApi.getCalorieNorm("Bearer YOUR_OPENAI_API_KEY", request)
//        val resultContent = response.choices.firstOrNull()?.message?.content ?: return
//
//        // Parse JSON manually or via Gson/Kotlinx
//        val calories = parseCaloriesFromJson(resultContent)

        // Update AppStorage
        appStorage.saveCalorieNorm(
            CalorieNorm(1, Calories(2300, 120, 70, 300), 2000 ))

        // Update MainActivityVM
        Log.e("abcd", "here" )
        mainActivityVM.updateCalorieNorm(CalorieNorm(1, Calories(2300, 120, 70, 300), 2000 ))
    }

//    private fun parseCaloriesFromJson(json: String): CalorieNorm {
//        // Assuming you're using kotlinx.serialization or Gson — example:
//        return Gson().fromJson(json, CalorieNorm::class.java)
//    }
}