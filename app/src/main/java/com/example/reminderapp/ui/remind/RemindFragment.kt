package com.example.reminderapp.ui.remind

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.reminderapp.R
import com.example.reminderapp.databinding.FragmentRemindBinding
import com.example.reminderapp.viewmodel.RemindViewModel


class RemindFragment : Fragment() {

    private val viewModel: RemindViewModel by viewModels()
    private lateinit var adapter: RemindAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentRemindBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = RemindAdapter(RemindClickListener { remindEntry ->
            findNavController().navigate(
                RemindFragmentDirections.actionRemindFragmentToEditFragment(
                    remindEntry
                )
            )
        })
        viewModel.getAllReminds.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        binding.apply {

            binding.recyclerView.adapter = adapter

            floatingActionButton.setOnClickListener{
                findNavController().navigate(R.id.action_remindFragment_to_addFragment)
            }
        }
        // Inflate the layout for this fragment
        return binding.root
    }

}