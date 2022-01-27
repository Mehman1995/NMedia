package ru.netology.nmedia.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.MainActivity
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepositoryInMemory

private val emptyPost = Post(
    id = 0,
    author = "",
    content = "",
    published = "",
    likesCount = 0,
    likedByMe = false,
    share = false,
    sharesCount = 0
)

class PostViewModel : ViewModel() {
    private val repository = PostRepositoryInMemory()
    val data = repository.get()
    val edited = MutableLiveData(emptyPost)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        edited.value = emptyPost
    }

    fun changeContent(content: String) {
        edited.value?.let {
            val text = content.trim()
            if (it.content != text)
                edited.value = it.copy(content = text)
        }
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun like(id: Long) = repository.like(id)
    fun share(id: Long) = repository.share(id)
    fun remove(id: Long) = repository.remove(id)
    fun cancelEditing() = edited.value?.let {
        repository.cancelEditing(it)
    }

}