package ru.netology.nmedia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.dto.Post

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        with(binding) {
            author.text = post.author
            published.text = post.published
            content.text = post.content

            like.isChecked = post.likedByMe
            like.text = "${post.likesCount}"

            repost.text = PostService.getCountText(post.repostsCount)
            viewing.text = PostService.getCountText(post.viewingsCount)

            like.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            repost.setOnClickListener {
                onInteractionListener.onRepost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            else -> false
                        }
                    }
                }.show()
            }

            if (!post.videoUrl.isNullOrBlank()) {
                videoGroup.visibility = View.VISIBLE

                val videoClickListener: (View) -> Context = { view ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.videoUrl))
                    view.context.apply {
                        val shareIntent =
                            Intent.createChooser(intent, getString(R.string.chooser_play_video))
                        startActivity(shareIntent)
                    }
                }

                videoButton.setOnClickListener { view -> videoClickListener(view) }
                videoPicture.setOnClickListener { view -> videoClickListener(view) }
            }
        }
    }
}