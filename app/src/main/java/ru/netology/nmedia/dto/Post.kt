package ru.netology.nmedia.dto

data class Post (
    val id: Long,
    val content: String ,
    val published: String ,
    val author: String,
    var likedByMe: Boolean = false,
    var likeCount: Int = 0,
    var repostsCount: Int = 0,
    var viewingsCount: Int = 0

        )

