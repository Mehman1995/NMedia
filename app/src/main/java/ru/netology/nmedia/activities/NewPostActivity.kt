package ru.netology.nmedia.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityNewPostBinding

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityNewPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val postId = intent.getLongExtra(Intent.EXTRA_UID, 0L)
        val oldContent = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (!oldContent.isNullOrBlank()) {
            binding.oldText.text = oldContent
            binding.edit.setText(oldContent)
            binding.editCancelGroup.visibility = View.VISIBLE
        }
        binding.edit.requestFocus();
        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()) {
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                val content = binding.edit.text.toString()
                intent.putExtra(Intent.EXTRA_UID, postId)
                intent.putExtra(Intent.EXTRA_TEXT, content)
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }
        binding.cancelEdit.setOnClickListener {
            finish()
        }
    }
}