package com.example.todolisttutorial

import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.todolisttutorial.databinding.FragmentNewTaskSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import java.time.LocalTime

class NewTaskSheet(var taskItem: TaskItem?) : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentNewTaskSheetBinding
    private lateinit var taskViewModel: TaskViewModel
    private var dueTime: LocalTime? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentNewTaskSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        taskViewModel = ViewModelProvider(requireActivity()).get(TaskViewModel::class.java)

        // Use a local variable to avoid smart cast issues
        val currentTask = taskItem

        binding.taskTitle.text = currentTask?.let {
            binding.name.text = Editable.Factory.getInstance().newEditable(it.name)
            binding.desc.text = Editable.Factory.getInstance().newEditable(it.desc)
            if (it.dueTime != null) {
                dueTime = it.dueTime
                updateTimeButtonText()
            }
            it.tags.forEach { tag -> addTagChip(tag) }
            "Edit Task"
        } ?: "New Task"

        binding.saveButton.setOnClickListener {
            val name = binding.name.text.toString()
            val desc = binding.desc.text.toString()
            val tags = currentTask?.tags ?: mutableListOf()
            val newTask = currentTask?.apply {
                this.name = name
                this.desc = desc
                this.dueTime = dueTime
            } ?: TaskItem(name, desc, dueTime, null, tags = tags)

            if (currentTask == null) taskViewModel.addTaskItem(newTask)
            else taskViewModel.updateTaskItem(newTask)

            dismiss()
        }
        binding.timePickerButton.setOnClickListener { openTimePicker() }
        binding.addTagButton.setOnClickListener {
            val tag = binding.tagInput.text.toString().trim()
            if (tag.isNotEmpty()) {
                addTagChip(tag)
                currentTask?.tags?.add(tag)
                binding.tagInput.setText("")
            }
        }
    }

    private fun addTagChip(tag: String) {
        val chip = Chip(context).apply {
            text = tag
            isClickable = false
            isCloseIconVisible = true
            setOnCloseIconClickListener { binding.chipGroup.removeView(this) }
        }
        binding.chipGroup.addView(chip)
    }

    private fun openTimePicker() {
        val listener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            dueTime = LocalTime.of(hour, minute)
            updateTimeButtonText()
        }
        TimePickerDialog(activity, listener, dueTime?.hour ?: 0, dueTime?.minute ?: 0, true).show()
    }

    private fun updateTimeButtonText() {
        binding.timePickerButton.text = String.format("%02d:%02d", dueTime?.hour, dueTime?.minute)
    }
}
