package com.example.todolisttutorial

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.time.LocalDate
import java.util.*

class TaskViewModel : ViewModel() {
    var taskItems = MutableLiveData<MutableList<TaskItem>>()

    init {
        taskItems.value = mutableListOf()
    }

    fun addTaskItem(taskItem: TaskItem) {
        val list = taskItems.value
        list?.add(taskItem)
        taskItems.postValue(list)
    }

    fun updateTaskItem(taskItem: TaskItem) {
        val list = taskItems.value
        list?.find { it.id == taskItem.id }?.apply {
            name = taskItem.name
            desc = taskItem.desc
            dueTime = taskItem.dueTime
            tags = taskItem.tags
        }
        taskItems.postValue(list)
    }

    fun setCompleted(taskItem: TaskItem) {
        val list = taskItems.value
        list?.find { it.id == taskItem.id }?.let {
            if (it.completedDate == null) {
                it.completedDate = LocalDate.now()
            }
        }
        taskItems.postValue(list)
    }
    fun deleteTaskItem(taskItem: TaskItem) {
        taskItems.value?.remove(taskItem)
        taskItems.postValue(taskItems.value)
    }
}
