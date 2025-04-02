package com.example.hotpot.models

data class Comment(
    val id: String,
    val author: String,
    val authorImageUrl: String,
    val text: String,
    val timestamp: String,
    val replies: List<Reply>,
)

data class Reply(
    val id: String,
    val author: String,
    val authorImageUrl: String,
    val text: String,
    val timestamp: String
)
