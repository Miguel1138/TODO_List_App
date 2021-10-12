package com.miguelsantos.todewit.ui.fragments.home

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.ItemTaskBinding
import com.miguelsantos.todewit.model.Task
import com.miguelsantos.todewit.ui.fragments.viewmodel.TaskViewModel

class TaskListAdapter(private val viewModel: TaskViewModel) :
    ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TaskViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_task,
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(viewModel, viewModel.taskList.value!![position])
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(viewModel: TaskViewModel, item: Task) {
            binding.apply {
                taskViewModel = viewModel
                task = item
                executePendingBindings()
            }

            // TODO Verificar possiveis implementações diretamente pelo layout.
            binding.itemImageMore.setOnClickListener { showPopUp(item) }
            setCardViewByOrientation(binding.root.resources.configuration.orientation)
        }

        // menu popUp
        @SuppressLint("RestrictedApi")
        private fun showPopUp(task: Task) {
            val imgMore = binding.itemImageMore
            PopupMenu(imgMore.context, imgMore).apply {
                menuInflater.inflate(R.menu.menu_popup_more, this.menu)
                setOnMenuItemClickListener { menu ->
                    when (menu.itemId) {
                        R.id.action_edit -> listenerEdit(task)
                        R.id.action_delete -> listenerDelete(task)
                    }
                    return@setOnMenuItemClickListener true
                }

                // Show Menu Icons
                if (this.menu is MenuBuilder) {
                    val menuBuilder = this.menu as MenuBuilder
                    menuBuilder.setOptionalIconsVisible(true)

                    for (item in menuBuilder.visibleItems) {
                        val iconMarginPx =
                            TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP,
                                4.0f,
                                binding.root.resources.displayMetrics
                            ).toInt()
                        if (item.icon != null && Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon = InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon = object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int =
                                    intrinsicHeight + iconMarginPx + iconMarginPx
                            }
                        }
                    }
                }
            }.show()
        }

        // Card view Configuration
        // disable onClicklistener in landScape mode, at least for now.
        private fun setCardViewByOrientation(orientation: Int) {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                binding.itemTaskDescription.visibility = View.VISIBLE
                binding.itemImageShowDesc.apply {
                    visibility = View.GONE
                    isClickable = false
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) isFocusable = false
                }
            } else {
                binding.itemImageShowDesc.apply {
                    visibility = View.VISIBLE
                    isClickable = true
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) isFocusable = true
                    setOnClickListener {
                        when (binding.itemTaskDescription.visibility) {
                            View.GONE, View.INVISIBLE-> {
                                binding.itemTaskDescription.visibility = View.VISIBLE
                                // Change icon image for the imageView.
                                this.setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                            }
                            View.VISIBLE -> {
                                binding.itemTaskDescription.visibility = View.GONE
                                this.setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                            }
                        }
                    }
                }
            }
        }

    }

}

class DiffCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

}


