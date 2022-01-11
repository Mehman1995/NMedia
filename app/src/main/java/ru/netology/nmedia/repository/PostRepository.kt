package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException

interface PostRepository {
    fun get(): LiveData<Post>
    fun like ()
    fun repost()
}

class PostRepositoryInMemoryImpl : PostRepository {
    private val defaultPost = Post(
        id = 1,
        author = "Нетология. Университет интернет-профессий будущего",
        content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
        published = "21 мая в 18:36",
        likesCount = 25
    )

    private val data = MutableLiveData(defaultPost)

    override fun get(): LiveData<Post> = data

    override fun like() {
        val post = getPostFromLiveData()
        val updatedPost = post.copy(
            likesCount = if (post.likedByMe) post.likesCount - 1 else post.likesCount + 1,
            likedByMe = !post.likedByMe
        )
        data.value = updatedPost
    }

    override fun repost() {
        val post = getPostFromLiveData()
        val updatedPost = post.copy(
            repostsCount = post.repostsCount + 1
        )
        data.value = updatedPost
    }

    private fun getPostFromLiveData() = data.value ?: throw RuntimeException("Data not specified")
}