package com.miguelsantos.todewit.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.FragmentHomeBinding
import com.miguelsantos.todewit.datasource.TaskDataSource
import com.miguelsantos.todewit.ui.TaskListAdapter
import com.miguelsantos.todewit.ui.fragments.AddTaskFragment.Companion.TASK_ID

class HomeFragment : Fragment() {

    companion object {
        private const val CREATE_MEW_TASK = 1010
    }

    private lateinit var binding: FragmentHomeBinding
    private val adapter: TaskListAdapter by lazy {
        TaskListAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Recycler View
        binding.homeFragmentRecyclerTasks.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
        binding.homeFragmentRecyclerTasks.adapter = adapter


        setListeners()
        updateList()

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_select_all -> {
                val list = TaskDataSource.getList()
                // Uncheck all the checkboxes in case they are all already selected.
                if (list.all { it.isDone }) {
                    list.forEach { it.isDone = false }
                } else {
                    list.filter { (!it.isDone) }
                        .forEach { it.isDone = true }

                }
                updateList()
                true
            }
            R.id.action_clear_finished_tasks -> {
                TaskDataSource.getList().apply { removeAll(filter { it.isDone }) }
                updateList()
                true
            }
            else -> false
        }
    }

    private fun updateList() {
        val list = TaskDataSource.getList()
        binding.emptyState.emptyStateConstraint.visibility =
            if (list.isEmpty()) View.VISIBLE else View.GONE
        adapter.notifyDataSetChanged()
        adapter.submitList(list)
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
            val bundle = Bundle().also {
                it.putInt(TASK_ID, task.id)
            }
            this.arguments = bundle
            findNavController().navigate(
                HomeFragmentDirections.actionHomeFragmentToAddTaskFragment()
            )
            // TODO: 28/07/2021 Pesquisar sobre data sharing entre fragmentos.
            //val intent = Intent(requireContext(), AddTaskFragment::class.java)
            //intent.putExtra(TASK_ID, task.id)
            //startActivityForResult(intent, CREATE_MEW_TASK)
        }

        adapter.listenerDelete = { task ->
            TaskDataSource.deleteTask(task)
            updateList()
        }
    }


}