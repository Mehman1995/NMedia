package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.repository.PostRepository
import ru.netology.nmedia.repository.PostRepositorySQLiteImpl

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

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepository =
        PostRepositorySQLiteImpl(AppDb.getInstance(application).postDao)

    val data = repository.getAll()
    val edited = MutableLiveData(emptyPost)

    fun save() {
        edited.value?.let {
            repository.save(it)
        }
        repository.saveDraft(null)
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

    fun like(id: Long) = repository.likedById(id)
    fun share(id: Long) = repository.shareById(id)
    fun remove(id: Long) = repository.removeById(id)
    fun cancelEditing() = edited.value?.let {
//        repository.cancelEditing(it)
    }

    fun saveDraft(draft: String?) = repository.saveDraft(draft)
    fun getDraft(): String? = repository.getDraft()

}