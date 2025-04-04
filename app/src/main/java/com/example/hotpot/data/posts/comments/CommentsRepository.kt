package com.example.hotpot.data.posts.comments

import com.example.hotpot.models.Article
import com.example.hotpot.models.Comment
import com.example.hotpot.models.PostItem

interface CommentsRepository {
    suspend fun getComments(id: String) : CommentsResult
    suspend fun postComment(id: String, request: CommentRequest) : Result
}
sealed class CommentsResult {
    data class Success(val comments: List<Comment>) : CommentsResult()
    data class Unauthorized(val code: Int, val message: String?) : CommentsResult()
    data class Error(val code: Int, val message: String?) : CommentsResult()
}
sealed class Result {
    data class Success(val status: String) : Result ()
    data class Unauthorized(val code: Int, val message: String?) : Result ()
    data class Error(val code: Int, val message: String?) : Result ()
}


