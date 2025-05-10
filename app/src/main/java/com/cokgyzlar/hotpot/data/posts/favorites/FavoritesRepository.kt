package com.cokgyzlar.hotpot.data.posts.favorites

interface FavoritesRepository {
    suspend fun makeFavorite(request: FavoriteRequest) : FavoritesResult
    suspend fun deleteFromFavorites(id: Int) : FavoritesResult
}
sealed class FavoritesResult {
    data class Success(val status: String) : FavoritesResult()
    data class Unauthorized(val code: Int, val message: String?) : FavoritesResult()
    data class Error(val code: Int, val message: String?) : FavoritesResult()
}