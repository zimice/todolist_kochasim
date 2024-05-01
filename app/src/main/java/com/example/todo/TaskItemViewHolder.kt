package com.example.todolisttutorial

import android.content.Context
import android.graphics.Paint
import androidx.recyclerview.widget.RecyclerView
import com.example.todolisttutorial.databinding.TaskItemCellBinding
import com.google.android.material.chip.Chip
import java.time.format.DateTimeFormatter

class TaskItemViewHolder(
    private val context: Context,
    private val binding: TaskItemCellBinding,
    private val clickListener: TaskItemClickListener
): RecyclerView.ViewHolder(binding.root) {
    private val timeFormat = DateTimeFormatter.ofPattern("HH:mm")

    fun bindTaskItem(taskItem: TaskItem) {
        binding.name.text = taskItem.name
        binding.name.paintFlags = if (taskItem.isCompleted()) Paint.STRIKE_THRU_TEXT_FLAG else 0
        binding.dueTime.text = taskItem.dueTime?.format(timeFormat) ?: ""
        binding.dueTime.paintFlags = if (taskItem.isCompleted()) Paint.STRIKE_THRU_TEXT_FLAG else 0

        binding.completeButton.setImageResource(taskItem.imageResource())
        binding.completeButton.setColorFilter(taskItem.imageColor(context))

        binding.completeButton.setOnClickListener { clickListener.completeTaskItem(taskItem) }
        binding.taskCellContainer.setOnClickListener { clickListener.editTaskItem(taskItem) }

        // Bind tags
        binding.chipGroup.removeAllViews()
        taskItem.tags.forEach { tag ->
            val chip = Chip(context).apply {
                text = tag
                isClickable = false
            }
            binding.chipGroup.addView(chip)
        }
    }
}
