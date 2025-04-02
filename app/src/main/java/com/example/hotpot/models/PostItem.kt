package com.example.hotpot.models

data class PostItem(
    val post_id: Int,
    val title: String,
    val author_username: String,
    val preview: String,
    var author_pfp: String,
    var is_favourite: Boolean)