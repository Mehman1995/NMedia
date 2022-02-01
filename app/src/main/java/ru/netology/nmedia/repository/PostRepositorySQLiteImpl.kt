package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post

class PostRepositorySQLiteImpl(private val dao: PostDao) :
    PostRepository {

    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }


    override fun getAll(): LiveData<List<Post>> = data

    override fun likedById(id: Long) {
        dao.likedById(id)
        posts = posts.map {

            if (it.id != id) it else {
                it.copy(
                    likedByMe = !it.likedByMe,
                    likesCount = if (it.likedByMe) it.likesCount - 1
                    else it.likesCount + 1
                )
            }
        }
        data.value = posts
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
        posts = posts.map {

            if (it.id != id) it else {
                val counter = it.sharesCount + 1
                if (it.id != id) it else it.copy(share = true, sharesCount = counter)
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter {
            it.id != id
        }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.save(post)

        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }

    override fun saveDraft(draft: String?) = dao.saveDraft(draft)

    override fun getDraft(): String? = dao.getDraft()
}