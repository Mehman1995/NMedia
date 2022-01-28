package ru.netology.nmedia.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.netology.nmedia.dto.Post

class PostRepositorySharedPreferenceImpl(context: Context) : PostRepository {

    private val pref = context.getSharedPreferences("repo", Context.MODE_PRIVATE)
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)
    private val gson = Gson()
    private val key = "posts"
    private val type = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var nextId = 1L

    init {
        pref.getString(key, null)?.let {
            posts = gson.fromJson(it, type)
            nextId = posts[0].id + 1
            data.value = posts
        }
    }

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {
        posts = posts.map {
            val counter = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1
            if (it.id != id) it else it.copy(likedByMe = !it.likedByMe, likesCount = counter)
        }

        data.value = posts
        syncing()
    }

    override fun share(id: Long) {
        posts = posts.map {
            val counter = it.sharesCount + 1
            if (it.id != id) it else it.copy(share = true, sharesCount = counter)
        }

        data.value = posts
        syncing()
    }

    override fun remove(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        syncing()
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++)) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }

        data.value = posts
        syncing()
    }

    override fun cancelEditing(post: Post) {
        posts.map { if (it.id != post.id) it }
        syncing()
    }

    private fun syncing() {
        with(pref.edit()) {
            putString(key, gson.toJson(posts))
            apply()
        }
    }
}