package com.example.reminderapp.ui.remind

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.reminderapp.R
import com.example.reminderapp.alarm.AlarmBroadcast
import com.example.reminderapp.database.RemindEntry
import com.example.reminderapp.databinding.FragmentRemindBinding
import com.example.reminderapp.viewmodel.RemindViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date


class RemindFragment : Fragment() {

    private val viewModel: RemindViewModel by viewModels()
    private lateinit var adapter: RemindAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.remind_menu, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete_all -> {
                        deleteAllItem()
                        true
                    }
                    R.id.action_load_reminds ->
                    {
                        fetchDataFromServer()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val binding = FragmentRemindBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        adapter = RemindAdapter(RemindClickListener { remindEntry ->
            findNavController().navigate(
                RemindFragmentDirections.actionRemindFragmentToUpdateFragment(
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
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val remindEntry = adapter.currentList[position]
                viewModel.delete(remindEntry)

                Snackbar.make(binding.root, "Deleted!", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo"){
                        viewModel.insert(remindEntry)
                    }
                    show()
                }
            }
        }).attachToRecyclerView(binding.recyclerView)
        return binding.root

    }

    private fun fetchDataFromServer() {
        val login = "mylogin"
        val token = "bd6e5304-dc9f-4164-a02a-58e667fc7999"
        lifecycleScope.launch {
            viewModel.getRemindsFromServer(token,login)
        }
    }

    private fun deleteAllItem(){
        AlertDialog.Builder(requireContext())
            .setTitle("Delete All")
            .setMessage("Are u sure?")
            .setPositiveButton("Yes"){dialog, _ ->
                viewModel.deleteAll()
                dialog.dismiss()
            }.setNegativeButton("No"){dialog, _ ->
                dialog.dismiss()
            }.create().show()
    }
    
}