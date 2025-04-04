package com.example.hotpot.data.posts.comments

import android.util.Log
import retrofit2.HttpException

class CommentsRepositoryImpl (
    private val api : CommentsApi
) : CommentsRepository{
    override suspend fun getComments(id: String): CommentsResult {
        return try{
            val response = api.getCommentsByPostId(id)
            Log.e("Repository", "success")
            CommentsResult.Success(response.data)
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            if(e.code()==401){
                CommentsResult.Unauthorized(e.code(), e.message())
            }else{
                CommentsResult.Error(e.code(), e.message())
            }
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            CommentsResult.Error(500, e.message)
        }
    }

    override suspend fun postComment(id: String, request: CommentRequest): Result {
        return try{
            val response = api.postComment(id, request)
            Log.e("Repository", "success")
            Result.Success("success")
        }catch(e: HttpException) {
            Log.e("Repository", "{${e.message()}}")
            if(e.code()==401){
                Result.Unauthorized(e.code(), e.message())
            }else{
                Result.Error(e.code(), e.message())
            }
        } catch (e: Exception) {
            Log.e("Repository", "{${e.message}}")
            Result.Error(500, e.message)
        }
    }

}