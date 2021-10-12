package com.miguelsantos.todewit.ui.fragments.home

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.data.application.TaskApplication
import com.miguelsantos.todewit.databinding.FragmentHomeBinding
import com.miguelsantos.todewit.ui.fragments.viewmodel.TaskViewModel
import com.miguelsantos.todewit.ui.fragments.viewmodel.TaskViewModelFactory

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: TaskViewModel
    private val adapter by lazy { TaskListAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        val viewModelFactory =
            TaskViewModelFactory((requireActivity().application as TaskApplication).repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TaskViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            taskViewModel = viewModel
            homeFragment = this@HomeFragment
        }

        //TODO Usar o observer diretamente pelo layout.
        viewModel.taskList.observe(viewLifecycleOwner, { tasks ->
            adapter.submitList(tasks)
        })

        binding.homeFragmentRecyclerTasks.layoutManager =
            GridLayoutManager(context, resources.getInteger(R.integer.grid_column_count))
        binding.homeFragmentRecyclerTasks.adapter = adapter

        setListeners()
    }

    fun fabClickListener() {
        findNavController().navigate(
            HomeFragmentDirections.actionHomeFragmentToAddTaskFragment()
        )
    }

    private fun setListeners() {
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
                setNegativeButton(R.string.label_cancel) { dialog, _ -> dialog.cancel() }
            }.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_finished_tasks -> {
                viewModel.clearCompletedTasks()
                true
            }

            //TODO refazer essa parte
            R.id.action_select_all -> {
                if (viewModel.areAllItemsChecked()) {
                    // retira a seleção de todos os itens caso todos já estejam selecionados.
                    viewModel.checkAllItems(false, 1)
                } else {
                    viewModel.checkAllItems(true)
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}