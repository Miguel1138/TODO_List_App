package com.miguelsantos.todewit.ui

import android.annotation.SuppressLint
import android.graphics.drawable.InsetDrawable
import android.os.Build
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuBuilder
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.miguelsantos.todewit.R
import com.miguelsantos.todewit.databinding.ItemTaskBinding
import com.miguelsantos.todewit.extensions.strike
import com.miguelsantos.todewit.model.Task

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(DiffCallback()) {

    var listenerEdit: (Task) -> Unit = {}
    var listenerDelete: (Task) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context))
        return TaskViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TaskViewHolder(private val binding: ItemTaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: Task) {
            binding.itemTaskTitle.text = task.title
            binding.itemTaskHour.text = task.hour
            binding.itemTaskDate.text = task.date
            binding.itemImageMore.setOnClickListener { showPopUp(task) }
            binding.itemTaskCheckDone.setOnClickListener { strikeThroughText() }
        }

        private fun strikeThroughText() {
            binding.itemTaskTitle.strike = binding.itemTaskCheckDone.isChecked
        }

        @SuppressLint("RestrictedApi")
        private fun showPopUp(task: Task) {
            val imgMore = binding.itemImageMore
            val popupMenu = PopupMenu(imgMore.context, imgMore)
            popupMenu.menuInflater.inflate(R.menu.menu_popup_more, popupMenu.menu)

            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.action_edit -> listenerEdit(task)
                    R.id.action_delete -> listenerDelete(task)
                }
                return@setOnMenuItemClickListener true
            }

            // Showing Menu Icons
            if (popupMenu.menu is MenuBuilder) {
                val menuBuilder = popupMenu.menu as MenuBuilder
                menuBuilder.setOptionalIconsVisible(true)
                for (item in menuBuilder.visibleItems) {
                    val iconMarginPx = TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        4.0f,
                        binding.root.resources.displayMetrics
                    ).toInt()
                    if (item.icon != null) {
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                            item.icon =
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0)
                        } else {
                            item.icon = object :
                                InsetDrawable(item.icon, iconMarginPx, 0, iconMarginPx, 0) {
                                override fun getIntrinsicWidth(): Int =
                                    intrinsicHeight + iconMarginPx + iconMarginPx
                            }
                        }
                    }
                }
            }

            popupMenu.show()
        }

    }

}

class DiffCallback : DiffUtil.ItemCallback<Task>() {

    override fun areItemsTheSame(oldItem: Task, newItem: Task) = oldItem == newItem

    override fun areContentsTheSame(oldItem: Task, newItem: Task) = oldItem.id == newItem.id

}


