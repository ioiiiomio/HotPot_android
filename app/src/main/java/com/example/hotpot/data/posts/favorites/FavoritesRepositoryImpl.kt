package com.example.hotpot.data.posts.favorites

import android.util.Log
import retrofit2.HttpException

class FavoritesRepositoryImpl (
    private val api : FavoritesApi
) : FavoritesRepository{
    override suspend fun makeFavorite(request: FavoriteRequest): FavoritesResult {
        return try{
            val response = api.makeFavorite(request)
            Log.e("Repository", "success")
            FavoritesResult.Success("success")
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            if(e.code()==401){
                FavoritesResult.Unauthorized(e.code(), e.message())
            }else{
                FavoritesResult.Error(e.code(), e.message())
            }
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            FavoritesResult.Error(500, e.message)
        }
    }

    override suspend fun deleteFromFavorites(id: Int): FavoritesResult {
        return try{
            val response = api.deleteFromFavorites(id.toString())
            Log.e("Repository", "success")
            FavoritesResult.Success("success")
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            if(e.code()==401){
                FavoritesResult.Unauthorized(e.code(), e.message())
            }else{
                FavoritesResult.Error(e.code(), e.message())
            }
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            FavoritesResult.Error(500, e.message)
        }
    }
}