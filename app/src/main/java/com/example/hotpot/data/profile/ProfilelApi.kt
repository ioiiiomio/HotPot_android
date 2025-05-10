package com.example.hotpot.data.profile

import com.example.hotpot.data.NoAuth
import com.example.hotpot.data.RequiresAuth
import com.example.hotpot.data.posts.comments.Response
import com.example.hotpot.models.DailyMeal
import com.example.hotpot.models.Dietician
import com.example.hotpot.models.HealthDetail
import com.example.hotpot.models.UserProfile
import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProfilelApi {
    @NoAuth
    @GET("user/api/v1/profile/id/{id}")
    suspend fun getUserProfileById(@Path("id") id: Int): UserProfile

    @NoAuth
    @GET("user/api/v1/profile/{username}")
    suspend fun getUserProfileByUsername(@Path("username") username: String): ProfileResponse

    @NoAuth
    @GET("user/api/v1/dietolog/profile/{id}")
    suspend fun getDietologById(@Path("id") id: Int): Dietician

    @NoAuth
    @GET("user/api/v1/dietolog/profile/{username}")
    suspend fun getDietologByUsername(@Path("username") username: String): DietologResponse

    @NoAuth
    @PUT("user/api/v1/profile/{username}")
    suspend fun updateProfile(@Path("username") username: String, @Body request : UpdateRequest): ProfileUpdateResponse

    @RequiresAuth
    @GET("user/api/v1/dietolog/dietologists")
    suspend fun getDietologists() : DietologistsResponse

    @NoAuth
    @GET("user/api/v1/user/{username}/follows")
    suspend fun getFollows(@Path("username") username: String) : FollowsResponse

    @RequiresAuth
    @POST("user/api/v1/users/follow")
    suspend fun followAction(@Body request: FollowRequest) : ProfileUpdateResponse

    @RequiresAuth
    @GET("user/api/v1/dietolog/{username}/appointments")
    suspend fun getAppointments(@Path("username") username : String) : AppointmentsResponse

    @RequiresAuth
    @POST("user/api/v1/dietolog/{username}/appointments")
    suspend fun createAppointment(@Path("username") username : String, @Body newApt : AppointmentRequest) : ProfileUpdateResponse

}

data class FollowRequest(
    val action: String,
    val target_username : String
)

data class FollowsResponse(
    val code : Int,
    val data : List<FollowData>,
    val message : String
)

data class FollowData(
    val user_id : Int,
    val username: String,
    val profile_picture: String
)

data class DietologResponse(
    val code : Int,
    val data : Dietician,
    val message : String
)

data class DietologistsResponse(
    val code : Int,
    val data : List<Dietician>,
    val message : String
)
data class ProfileResponse(
    val code : Int,
    val data : UserProfile,
    val message : String
)

data class ProfileUpdateResponse(
    val code : Int,
    val data : Data,
    val message : String
)

data class Data(
    val message: String
)

data class UpdateRequest(
    val birth_date  : String,
    val health_details : List<HealthDetail>,
    val profile_picture : String,
    val sex : String,
    val vision: List<String>
)

data class AppointmentsResponse(
    val code : Int,
    val data : List<Appointment>,
    val message: String
)

data class Appointment(
    val id : Int? = null,
    val title : String,
    val time : String,
    val date: String,
    var client: Int? = null
)

data class AppointmentRequest(
    val title: String,
    val date: String,
    val time: String
)

