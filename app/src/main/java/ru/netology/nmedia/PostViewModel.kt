package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositoryInMemoryImpl

private val emptyPost = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
)

class PostViewModel : ViewModel() {
    private val repository: PostRepository = PostRepositoryInMemoryImpl()
    val data: LiveData<List<Post>> = repository.get()
    val editedPost = MutableLiveData(emptyPost)

    fun likeById(id: Long) = repository.likeById(id)
    fun repostById(id: Long) = repository.repostById(id)
    fun removeById(id: Long) = repository.removeById(id)

    fun save() {
        editedPost.value?.let { post ->
            repository.save(post)
        }
        editedPost.value = emptyPost
    }

    fun edit(post: Post) {
        editedPost.value = post
    }

    fun changeContent(content: String) {
        editedPost.value?.let { post ->
            val text = content.trim()
            if (post.content == text) {
                return
            }
            editedPost.value = post.copy(content = text)
        }
    }

    fun cancelEdit() {
        editedPost.value = emptyPost
    }
}