package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.nmedia.dto.Post

class PostRepositoryInMemory : PostRepository {
    private var nextId = 1L
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущег",
            content = "Нажми быстрее",
            published = "21 мая в 19:37",
            likesCount = 1,
            sharesCount = 2,
            video = "https://www.youtube.com/watch?v=WhWc3b3KhnY"
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущег",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb\"",
            published = "22 мая в 18:36",
            likesCount = 999,
            sharesCount = 996
        ),
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущег",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия — помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb\"",
            published = "23 мая в 18:36",
            likesCount = 1600,
            sharesCount = 1600
        )
    ).reversed()

    private val data = MutableLiveData(posts)

    override fun get(): LiveData<List<Post>> = data

    override fun like(id: Long) {

        posts = posts.map {
            val counter = if (it.likedByMe) it.likesCount - 1 else it.likesCount + 1
            if (it.id != id) it else it.copy(likedByMe = !it.likedByMe, likesCount = counter)
        }

        data.value = posts
    }

    override fun share(id: Long) {

        posts = posts.map {
            val counter = it.sharesCount + 1
            if (it.id != id) it else it.copy(share = true, sharesCount = counter)
        }

        data.value = posts
    }

    override fun remove(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {

        posts = if (post.id == 0L) {
            listOf(post.copy(id = nextId++)) + posts
        } else {
            posts.map { if (it.id != post.id) it else it.copy(content = post.content) }
        }

        data.value = posts
    }

    override fun cancelEditing(post: Post) {
        posts.map { if (it.id != post.id) it }
    }


}
