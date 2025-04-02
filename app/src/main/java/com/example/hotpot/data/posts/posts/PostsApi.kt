package com.example.hotpot.data.posts.posts

import com.example.hotpot.data.NoAuth
import com.example.hotpot.data.RequiresAuth
import com.example.hotpot.models.Article
import com.example.hotpot.models.PostItem
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PostsApi {
    @NoAuth
    @GET("/posts/api/v1/posts")
    suspend fun getPosts() : List<Article>

    @NoAuth
    @GET("/posts/api/v1/posts/{id}")
    suspend fun getPostById(@Path("id") id: String): Article

    @RequiresAuth
    @GET("/posts/api/v1/protected/posts")
    suspend fun getFeed(): FeedResponse
}
data class FeedResponse(
    val code : Int,
    val data : List<PostItem>,
    val message : String
)
