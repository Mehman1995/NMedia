package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post
import java.lang.RuntimeException

interface PostRepository {
    fun get(): LiveData<List<Post>>
    fun likeById(id: Long)
    fun repostById(id: Long)
    fun removeById(id: Long)
    fun save(post: Post)
}

class PostRepositoryInMemoryImpl : PostRepository {
    private val defaultPosts = listOf(
        Post(
            id = 5,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likesCount = 10,
            repostsCount = 1
        ),
        Post(
            id = 4,
            author = "Нетология",
            content = "Затем появились курсы по дизайну, разработке, аналитике и управлению",
            published = "21 мая в 18:36",
            likesCount = 2,
            repostsCount = 0
        ),
        Post(
            id = 3,
            author = "Университет интернет-профессий будущего",
            content = "Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу",
            published = "21 мая в 18:36",
            likesCount = 120,
            repostsCount = 10
        ),
        Post(
            id = 2,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология!",
            published = "21 мая в 18:36",
            likesCount = 49,
            repostsCount = 99
        ),
        Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            likesCount = 999,
            repostsCount = 999
        )
    )

    private var nextId = 1L

    private val data = MutableLiveData(defaultPosts)

    override fun get(): LiveData<List<Post>> = data

    override fun likeById(id: Long) {
        val updater = { post: Post ->
            if (post.id == id)
                post.copy(
                    likesCount = if (post.likedByMe) post.likesCount - 1 else post.likesCount + 1,
                    likedByMe = !post.likedByMe
                )
            else
                post
        }
        updatePost(updater)
    }

    override fun repostById(id: Long) {
        val updater = { post: Post ->
            if (post.id == id)
                post.copy(repostsCount = post.repostsCount + 1)
            else
                post
        }
        updatePost(updater)
    }

    override fun removeById(id: Long) {
        val posts = getPostsFromLiveData()
        val updatedPosts = posts.filter { post -> post.id != id }
        data.value = updatedPosts
    }

    override fun save(post: Post) {
        if (post.id == 0L) {
            val newPost = post.copy(
                id = nextId++,
                author = "Me",
                published = "now"
            )
            val posts = listOf(newPost) + getPostsFromLiveData()
            data.value = posts
            return
        }

        val updater = { oldPost: Post ->
            if (oldPost.id == post.id)
                oldPost.copy(content = post.content)
            else
                oldPost
        }
        updatePost(updater)
    }

    private fun updatePost(updater: (Post) -> Post) {
        val posts = getPostsFromLiveData()
        val updatedPosts = posts.map(updater)
        data.value = updatedPosts
    }

    private fun getPostsFromLiveData() = data.value ?: throw RuntimeException("Data not specified")
}