package com.miguelsantos.todewit.ui.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.FragmentHomeBinding
import com.miguelsantos.todewit.datasource.application.TaskApplication
import com.miguelsantos.todewit.ui.fragments.TaskViewModel
import com.miguelsantos.todewit.ui.fragments.TaskViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: TaskViewModel
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val viewModelFactory =
            TaskViewModelFactory((requireActivity().application as TaskApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)

        // Recycler View
        binding.homeFragmentRecyclerTasks.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
        binding.homeFragmentRecyclerTasks.adapter = adapter

        setObservers()
        setListeners()
        setHasOptionsMenu(true)

        return binding.root
    }

    /**
     * Change the layout if the list is not empty
     */
    private fun setObservers() {
        viewModel.taskList.observe(viewLifecycleOwner, { tasks ->
            adapter.submitList(tasks)
            if (tasks.isNotEmpty()) {
                binding.emptyState.emptyStateConstraint.visibility = View.GONE
                binding.homeFragmentRecyclerTasks.visibility = View.VISIBLE
            } else if (tasks.isNullOrEmpty()) {
                binding.emptyState.emptyStateConstraint.visibility = View.VISIBLE
            }
        })
    }


    private fun setListeners() {
        // Fab
        binding.fabAddTask.setOnClickListener {
            it.findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddTaskFragment()
            )
        }

        // item_task listeners
        adapter.listenerEdit = { task ->
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddTaskFragment().setTask(task)
            )
        }

        adapter.listenerDelete = { task ->
            MaterialAlertDialogBuilder(requireContext()).apply {
                setTitle(R.string.dialog_title)
                setMessage(R.string.dialog_text)
                setPositiveButton(R.string.action_delete) { _, _ -> viewModel.deleteTask(task) }
                setNegativeButton(R.string.label_cancel) { _, _ -> }
            }.show()
        }
    }

}