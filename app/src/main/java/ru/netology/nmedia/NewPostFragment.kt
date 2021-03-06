package ru.netology.nmedia

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.viewmodel.PostViewModel
import androidx.activity.addCallback

class NewPostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentNewPostBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment
        )

        val content = arguments?.getString("content")

        binding.edit.setText(content)
        binding.edit.requestFocus()

        viewModel.getDraft()?.let(binding.edit::setText)


        val callback = requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            viewModel.saveDraft(binding.edit.text.toString())
            remove()
            requireActivity().onBackPressed()
        }


        binding.ok.setOnClickListener {

            val content = binding.edit.text.toString()
            viewModel.changeContent(content)
            viewModel.save()
            findNavController().navigate(R.id.action_newPostFragment_to_feedFragment)
        }

        return binding.root
    }

}