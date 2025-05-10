package com.example.hotpot.models


data class UserProfile(
    val user_id : Int,
    val name: String,
    val surname: String,
    val username: String,
    val follows: Int,
    val profile_picture: String,
    val birth_date: String?,
    val sex: String?,
    var health_details: List<HealthDetail>?,
    val vision: List<String>
)

data class HealthDetail(
    val height: Int,
    val weight: Double,
    var created_at: String?
)

data class Dietician(
    var is_following : Boolean?,
    val user_id: Int,
    val name: String,
    val surname: String,
    val username: String,
    val followers: Int,
    val posts: Int,
    val premium_subscribers: Int,
    val profile_picture: String,
    val occupation: String,
    val experience: List<GuideItem>,
    val about: String,
    val experience_years: String,
    val certificates: List<GuideItem>
)

data class GuideItem(
    val title: String,
    val institution : String,
    val year: String,
    val description: String
)

