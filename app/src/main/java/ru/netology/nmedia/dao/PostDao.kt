package ru.netology.nmedia.dao

import ru.netology.nmedia.dto.Post

interface PostDao {
    fun getAll(): List<Post>
    fun save(post: Post): Post
    fun likedById(id: Long)
    fun removeById(id: Long)
    fun shareById(id: Long)
    fun saveDraft(draft: String?)
    fun getDraft() :String?
}