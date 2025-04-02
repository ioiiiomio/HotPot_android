package com.example.hotpot.models

data class ArticleContent (
    val type : String,
    val data : String
)

data class Article (
    val id: Int,
    val author: String,
    val content: List<ArticleContent>,
    val tags: List<String>,
    val title: String
)