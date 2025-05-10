package com.cokgyzlar.hotpot.data.posts.comments

import com.cokgyzlar.hotpot.data.NoAuth
import com.cokgyzlar.hotpot.data.RequiresAuth
import com.cokgyzlar.hotpot.models.Comment
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CommentsApi {
    @NoAuth
    @GET("posts/api/v1/posts/{id}/comments")
    suspend fun getCommentsByPostId(@Path("id") id: String): CommentsResponse

    @RequiresAuth
    @POST("posts/api/v1/protected/posts/{id}/comments")
    suspend fun postComment(@Path("id") id: String, @Body request: CommentRequest): Response
}
data class CommentRequest(
    val content: String
)
data class CommentsResponse(
    val code: Int,
    val data: List<Comment>,
    val message: String
)
data class Response(
    val code: Int,
    val data: ResponseStatus,
    val message: String
)
data class ResponseStatus(
    val status: String
)