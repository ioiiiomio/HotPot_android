package com.example.hotpot.data.posts.posts

import com.example.hotpot.models.Article
import com.example.hotpot.models.PostItem

interface PostsRepository {
    suspend fun getPosts() : PostsResult
    suspend fun getFeed() : FeedResult
    suspend fun getPostById(id: Int): ArticleResult
}
sealed class PostsResult {
    data class Success(val posts: List<Article>) : PostsResult()
    data class Unauthorized(val code: Int, val message: String?) : PostsResult()
    data class Error(val code: Int, val message: String?) : PostsResult()
}

sealed class FeedResult {
    data class Success(val postsPreviews: List<PostItem>) : FeedResult()
    data class Unauthorized(val code: Int, val message: String?) : FeedResult()
    data class Error(val code: Int, val message: String?) : FeedResult()
}

sealed class ArticleResult {
    data class Success(val article: Article) : ArticleResult()
    data class Unauthorized(val code: Int, val message: String?) : ArticleResult()
    data class Error(val code: Int, val message: String?) : ArticleResult()
}