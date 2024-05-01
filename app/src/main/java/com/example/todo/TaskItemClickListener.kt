package com.example.todo

interface TaskItemClickListener
{
    fun editTaskItem(taskItem: TaskItem)
    fun completeTaskItem(taskItem: TaskItem)
    fun deleteTaskItem(taskItem: TaskItem)
}