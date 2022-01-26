package ru.netology.nmedia.activities

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.dto.Post

class NewPostResultContract : ActivityResultContract<Post?, Pair<Long, String>?>() {

    override fun createIntent(context: Context, input: Post?): Intent {
        val intent = Intent(context, NewPostActivity::class.java)
        if (input != null) {
            intent.putExtra(Intent.EXTRA_UID, input.id)
            intent.putExtra(Intent.EXTRA_TEXT, input.content)
        }
        return intent
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Pair<Long, String>? =
        if (resultCode == Activity.RESULT_OK) {
            val postId = intent?.getLongExtra(Intent.EXTRA_UID, 0L) ?: 0L
            val newContent = intent?.getStringExtra(Intent.EXTRA_TEXT) ?: ""
            Pair(postId, newContent)
        } else {
            null
        }
}
