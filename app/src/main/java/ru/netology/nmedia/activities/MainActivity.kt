package ru.netology.nmedia.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.OnInteractionListener
import ru.netology.nmedia.PostAdapter
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.dto.Post

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels()

        val newPostLauncher = registerForActivityResult(NewPostResultContract()) { result ->
            result ?: return@registerForActivityResult
            viewModel.save(result.first, result.second)
        }

        val adapter = PostAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) =
                viewModel.likeById(post.id)

            override fun onRepost(post: Post) =
                viewModel.repostById(post.id)

            override fun onEdit(post: Post) {
                newPostLauncher.launch(post)
            }

            override fun onRemove(post: Post) =
                viewModel.removeById(post.id)
        })

        binding.list.adapter = adapter

        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
        }

        binding.fab.setOnClickListener {
            newPostLauncher.launch(null)
        }
    }
}