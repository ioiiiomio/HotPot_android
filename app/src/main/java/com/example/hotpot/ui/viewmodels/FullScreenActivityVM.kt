package com.example.hotpot.ui.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.hotpot.models.Dietician
import com.example.hotpot.models.HealthDetail
import com.example.hotpot.models.PostItem
import com.example.hotpot.models.UserProfile
import java.sql.Timestamp
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.Date

class FullScreenActivityVM : ViewModel() {
    var userProfile = MutableLiveData<UserProfile>()
    var dieticianProfile = MutableLiveData<Dietician>()
    var posts = MutableLiveData<List<PostItem>>()

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
    }

    fun updateVisions(visions : List<String>){
        val currentProfile = userProfile.value ?: return
        val updatedProfile = currentProfile.copy(
            vision = visions
        )
        userProfile.value=updatedProfile
    }
}