package com.cokgyzlar.hotpot.data.profile

import com.cokgyzlar.hotpot.models.Dietician
import com.cokgyzlar.hotpot.models.UserProfile

interface ProfileRepository {
    suspend fun getUser(id: Int) : UserResult
    suspend fun getDietician(id: Int) : DieticianResult
    suspend fun getUser(username : String) : UserResult
    suspend fun getDietician(username: String) : DieticianResult
    suspend fun updateProfile(username: String, updateRequest: UpdateRequest) : UpdateResult
    suspend fun getDieticians() : DieticiansResult
    suspend fun getFollows(username: String) : FollowsResult
    suspend fun follow(username: String) : UpdateResult
    suspend fun unfollow(username: String) : UpdateResult
    suspend fun getAppointments(username: String) : AppointmentResult
    suspend fun createAppointment(username: String, appt: AppointmentRequest) : UpdateResult
}
sealed class FollowsResult{
    data class Success(val follows: List<FollowData>) : FollowsResult()
    data class Error(val code: Int, val message: String?) : FollowsResult()
}
sealed class UserResult {
    data class Success(val user : UserProfile) : UserResult()
    data class Error(val code: Int, val message: String?) : UserResult()
}
sealed class DieticianResult {
    data class Success(val dietician: Dietician) :  DieticianResult()
    data class Error(val code: Int, val message: String?) :  DieticianResult()
}
sealed class UpdateResult {
    data class Success(val status : String) : UpdateResult()
    data class Error(val code: Int, val message: String?) :  UpdateResult()
}
sealed class DieticiansResult {
    data class Success(val dieticians: List<Dietician>) :  DieticiansResult()
    data class Error(val code: Int, val message: String?) :  DieticiansResult()
}
sealed class AppointmentResult {
    data class Success(val appointments: List<Appointment>) :  AppointmentResult()
    data class Error(val code: Int, val message: String?) :   AppointmentResult()
}
