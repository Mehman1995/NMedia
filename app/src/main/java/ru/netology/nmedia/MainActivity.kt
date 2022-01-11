package ru.netology.nmedia

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()
        viewModel.data.observe(this) { post ->
            updateView(binding, post)
        }

        with(binding) {
            likesImage.setOnClickListener {
                viewModel.like()
            }
            repostsImage.setOnClickListener {
                viewModel.repost()
            }
        }
    }

    private fun updateView(binding: ActivityMainBinding, post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            val likesImageResource =
                if (post.likedByMe) R.drawable.ic_likes else R.drawable.ic_likes_24
            likesImage.setImageResource(likesImageResource)

            likesText.text = PostService.getCountText(post.likesCount)
            repostsText.text = PostService.getCountText(post.repostsCount)
            viewingsText.text = PostService.getCountText(post.viewingsCount)
        }
    }
}