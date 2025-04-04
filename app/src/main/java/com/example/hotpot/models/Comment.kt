package com.example.hotpot.models

data class Comment(
    val id: Int,
    val post_id: Int,
    val author: String,
    val authorImageUrl: String?,
    val content: String,
    val created_at: String,
    val updated_at: String,
    var replies: List<Reply>?
)

data class Reply(
    val id: String,
    val author: String,
    val authorImageUrl: String,
    val text: String,
    val timestamp: String
)
