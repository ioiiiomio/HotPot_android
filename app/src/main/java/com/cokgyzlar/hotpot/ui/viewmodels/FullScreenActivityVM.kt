package com.cokgyzlar.hotpot.ui.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cokgyzlar.hotpot.data.posts.posts.PostRequest
import com.cokgyzlar.hotpot.data.posts.posts.PostsRepository
import com.cokgyzlar.hotpot.data.posts.posts.Result
import com.cokgyzlar.hotpot.data.profile.Appointment
import com.cokgyzlar.hotpot.data.profile.AppointmentRequest
import com.cokgyzlar.hotpot.data.profile.ProfileRepository
import com.cokgyzlar.hotpot.data.profile.UpdateRequest
import com.cokgyzlar.hotpot.data.profile.UpdateResult
import com.cokgyzlar.hotpot.data.profile.UserResult
import com.cokgyzlar.hotpot.data.model.CalorieNorm
import com.cokgyzlar.hotpot.data.model.Calories
import com.cokgyzlar.hotpot.data.model.UserGoal
import com.cokgyzlar.hotpot.models.Dietician
import com.cokgyzlar.hotpot.models.HealthDetail
import com.cokgyzlar.hotpot.models.PostItem
import com.cokgyzlar.hotpot.models.UserProfile
import com.prowheelxrassistv01.data.AppStorage
import kotlinx.coroutines.launch
import org.koin.mp.KoinPlatform.getKoin
import java.sql.Timestamp

class FullScreenActivityVM : ViewModel() {
    var userProfile = MutableLiveData<UserProfile>()
    var dieticianProfile = MutableLiveData<Dietician>()
    var posts = MutableLiveData<List<PostItem>>()
    var reloadDieticianProfile = MutableLiveData<Boolean>(false)
    private val appStorage: AppStorage by lazy { getKoin().get<AppStorage>()}
    private val mainActivityVM: MainActivityVM by lazy { getKoin().get<MainActivityVM>()}
    private val profileRepository: ProfileRepository by lazy { getKoin().get<ProfileRepository>() }
    private val postsRepository: PostsRepository by lazy { getKoin().get<PostsRepository>() }


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
            health_details = currentProfile.health_details?.plus(newHealthDetail)
        )

        userProfile.value = updatedProfile
        userProfile.value!!.health_details?.let { appStorage.saveHealthDetail(it) }
        Log.e("abcd", appStorage.getHealthDetail().toString())
        updateProfile()
        viewModelScope.launch {
            generateAndUpdateCalorieNorm()
        }
    }

    fun updateProfile(){
        viewModelScope.launch {
            try {
                val currentProfile = userProfile.value
                currentProfile?.health_details?.map { it.created_at = null }
                Log.e("currentprofforupd", currentProfile.toString())
                val result = profileRepository.updateProfile(
                    currentProfile!!.username.drop(1),
                    UpdateRequest(
                        currentProfile.birth_date!!,
                        currentProfile.health_details!!,
                        currentProfile.profile_picture,
                        currentProfile.sex!!,
                        currentProfile.UserGoal!!))
                if (result is UpdateResult.Success) {
                    Log.d("PostDebug", "Profile update successful")
                } else {
                    Log.e("PostDebug", "Profile update failed: $result")
                }
            } catch (e: Exception) {
                Log.e("PostDebug", "Exception during post: ${e.message}", e)
            }
        }
    }

    fun updateUserGoal(userGoal : UserGoal){
        val currentProfile = userProfile.value ?: return
        val updatedProfile = currentProfile.copy(
            UserGoal = userGoal
        )
        userProfile.value=updatedProfile
        updateProfile()
        viewModelScope.launch {
            generateAndUpdateCalorieNorm()
        }
    }

    fun fetchUserById(id: Int) {
        viewModelScope.launch {
            val result = profileRepository.getUser(id)
            if (result is UserResult.Success) {
                userProfile.postValue(result.user)
            }
        }
    }

    fun fetchUserByUsername(username: String) {
        viewModelScope.launch {
            val result = profileRepository.getUser(username)
            if (result is UserResult.Success) {
                userProfile.postValue(result.user)
            }
        }
    }

    fun post(post: PostRequest){
        viewModelScope.launch {
            try {
                val result = postsRepository.post(post)
                if (result is Result.Success) {
                    Log.d("PostDebug", "Post successful")
                    reloadDieticianProfile.postValue(true)
                } else {
                    Log.e("PostDebug", "Post failed: $result")
                }
            } catch (e: Exception) {
                Log.e("PostDebug", "Exception during post: ${e.message}", e)
            }
        }
    }

    fun createAppointment(username: String, newAppt: Appointment){
        viewModelScope.launch {
            try {
                val result = profileRepository.createAppointment(username, AppointmentRequest(newAppt.title, newAppt.date, newAppt.time))
                if (result is UpdateResult.Success) {
                    Log.d("PostDebug", "Post successful")
                    reloadDieticianProfile.postValue(true)
                } else {
                    Log.e("PostDebug", "Post failed: $result")
                }
            } catch (e: Exception) {
                Log.e("PostDebug", "Exception during post: ${e.message}", e)
            }
        }
    }

    suspend fun generateAndUpdateCalorieNorm() {
        if(userProfile.value == null) return
        if(userProfile.value!!.health_details == null || userProfile.value!!.health_details!!.isEmpty()) return
        if(userProfile.value!!.UserGoal == null) return // Updated to check userGoal
        if(userProfile.value!!.sex == null ||  userProfile.value!!.sex!!.isEmpty()) return
        if(userProfile.value!!.birth_date == null || userProfile.value!!.birth_date!!.endsWith("1900")) return
        try{
            val result = profileRepository.updateProfile(
                userProfile.value!!.username,
                UpdateRequest(
                    userProfile.value!!.birth_date!!,
                    userProfile.value!!.health_details!!,
                    userProfile.value!!.profile_picture,
                    userProfile.value!!.sex!!,
                    userProfile.value!!.UserGoal!!)) // Updated to use userGoal
        }catch (e: Exception){
            return
        }

        // Update AppStorage
        appStorage.saveCalorieNorm(
            CalorieNorm(1, Calories(2300, 120, 70, 300), 2000)
        )

        // Update MainActivityVM
        Log.e("abcd", "here")
        mainActivityVM.updateCalorieNorm(CalorieNorm(1, Calories(2300, 120, 70, 300), 2000))
    }
}
