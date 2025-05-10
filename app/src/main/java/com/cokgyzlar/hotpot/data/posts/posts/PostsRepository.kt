package com.cokgyzlar.hotpot.data.posts.posts

import com.cokgyzlar.hotpot.models.Article
import com.cokgyzlar.hotpot.models.PostItem

interface PostsRepository {
    suspend fun getPosts() : PostsResult
    suspend fun getFeed() : FeedResult
    suspend fun getPostById(id: Int): ArticleResult
    suspend fun post(post: PostRequest): Result
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

sealed class Result {
    data class Success(val status: String): Result()
    data class Unauthorized(val code: Int, val message: String?): Result()
    data class Error(val code: Int, val message: String?): Result()
}