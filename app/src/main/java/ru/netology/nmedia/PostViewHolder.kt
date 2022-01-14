package ru.netology.nmedia

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

            val likesImageResource =
                if (post.likedByMe) R.drawable.ic_liked_24 else R.drawable.ic_likes_24
            likesImage.setImageResource(likesImageResource)

            likesText.text = PostService.getCountText(post.likesCount)
            repostsText.text = PostService.getCountText(post.repostsCount)
            viewingsText.text = PostService.getCountText(post.viewingsCount)

            likesImage.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            repostsImage.setOnClickListener {
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
        }
    }
}