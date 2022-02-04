package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.DraftEntity
import ru.netology.nmedia.entity.PostEntity

class PostRepositorySQLiteImpl(private val dao: PostDao) :
    PostRepository {

    override fun getAll(): LiveData<List<Post>> = Transformations.map(dao.getAll()){ listPostEntity ->
        listPostEntity.map {
            it.toDto()
        }
    }

    override fun likedById(id: Long) {
        dao.likedById(id)
    }

    override fun shareById(id: Long) {
        dao.shareById(id)
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
    }

    override fun save(post: Post) {
        dao.save(PostEntity.fromDto(post))
    }

    override fun saveDraft(draft: String?) {
        dao.saveDraft(draft)
    }

    override fun getDraft(): String?{
        return dao.getDraft()
    }
}