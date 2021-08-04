package com.miguelsantos.todewit.ui.fragments.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.FragmentHomeBinding
import com.miguelsantos.todewit.datasource.application.TaskApplication
import com.miguelsantos.todewit.ui.TaskListAdapter
import com.miguelsantos.todewit.ui.fragments.TaskViewModel
import com.miguelsantos.todewit.ui.fragments.TaskViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter()
    }
    private lateinit var viewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val viewModelFactory =
            TaskViewModelFactory((requireActivity().application as TaskApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)
        observers()

        // Recycler View
        binding.homeFragmentRecyclerTasks.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
        binding.homeFragmentRecyclerTasks.adapter = adapter

        setListeners()
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_all -> {
                viewModel.selectAll()
                true
            }
            R.id.action_clear_finished_tasks -> {
                viewModel.deleteCompletedTasks()
                true
            }
            else -> false
        }
    }

    private fun observers() {
        viewModel.taskList.observe(viewLifecycleOwner, { tasks ->
            binding.emptyState.emptyStateConstraint.visibility =
                if (tasks.isNullOrEmpty()) View.VISIBLE else View.GONE
            adapter.notifyDataSetChanged()
            adapter.submitList(tasks)
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
            viewModel.deleteTask(task)
        }
    }


}