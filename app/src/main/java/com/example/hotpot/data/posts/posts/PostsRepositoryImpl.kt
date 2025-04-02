package com.example.hotpot.data.posts.posts

import android.util.Log
import retrofit2.HttpException

class PostsRepositoryImpl (
    private val api : PostsApi
) : PostsRepository{
    val IMAGE_IRL = "https://norma-center.ru/assets/uploads/images/u/89/t/dietolog-1562314436.jpg"
    override suspend fun getPosts(): PostsResult {
        return try{
            val response = api.getPosts()
            Log.e("Repository", "success")
            PostsResult.Success(response)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            if(e.code()==401){
                PostsResult.Unauthorized(e.code(), e.message())
            }else{
                PostsResult.Error(e.code(), e.message())
            }
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            PostsResult.Error(500, e.message)
        }
    }

    override suspend fun getFeed(): FeedResult {
        return try{
            val response = api.getFeed()
            Log.e("Repository", "success")
            val updatedData = response.data.map { item ->
                item.author_pfp = IMAGE_IRL
                item
            }
            FeedResult.Success(updatedData)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            if(e.code()==401){
                FeedResult.Unauthorized(e.code(), e.message())
            }else{
                FeedResult.Error(e.code(), e.message())
            }
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            FeedResult.Error(500, e.message)
        }
    }
}