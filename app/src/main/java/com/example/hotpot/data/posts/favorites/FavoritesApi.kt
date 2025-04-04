package com.example.hotpot.data.posts.favorites

import com.example.hotpot.data.NoAuth
import com.example.hotpot.data.RequiresAuth
import com.example.hotpot.models.Article
import com.example.hotpot.models.PostItem
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface FavoritesApi {
    @RequiresAuth
    @DELETE("/posts/api/v1/protected/favorites/{id}")
    suspend fun deleteFromFavorites(@Path("id") id: String)

    @RequiresAuth
    @POST("/posts/api/v1/protected/favorites")
    suspend fun makeFavorite(@Body request: FavoriteRequest): FavoriteResponce
}

data class FavoriteRequest(
    val post_id : Int
)

data class FavoriteResponce(
    val idk : String
)
