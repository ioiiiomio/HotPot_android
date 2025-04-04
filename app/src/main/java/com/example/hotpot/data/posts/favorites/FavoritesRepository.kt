package com.example.hotpot.data.posts.favorites

import com.example.hotpot.models.Article
import com.example.hotpot.models.PostItem

interface FavoritesRepository {
    suspend fun makeFavorite(request: FavoriteRequest) : FavoritesResult
    suspend fun deleteFromFavorites(id: Int) : FavoritesResult
}
sealed class FavoritesResult {
    data class Success(val status: String) : FavoritesResult()
    data class Unauthorized(val code: Int, val message: String?) : FavoritesResult()
    data class Error(val code: Int, val message: String?) : FavoritesResult()
}