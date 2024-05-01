package com.example.todolisttutorial

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolisttutorial.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), TaskItemClickListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        taskViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        binding.newTaskButton.setOnClickListener {
            NewTaskSheet(null).show(supportFragmentManager, "newTaskTag")
        }

        setRecyclerView()
    }

    private fun setRecyclerView() {
        taskViewModel.taskItems.observe(this) { taskItems ->
            binding.todoListRecyclerView.apply {
                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = TaskItemAdapter(taskItems, this@MainActivity)
            }
        }

        val itemTouchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            override fun onMove(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder): Boolean {
                return false // We are not handling move operations
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val taskItem = (binding.todoListRecyclerView.adapter as TaskItemAdapter).taskItems[position]

                AlertDialog.Builder(this@MainActivity)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this task?")
                    .setPositiveButton("Delete") { dialog, which ->
                        // Remove the item from the ViewModel and notify the adapter
                        taskViewModel.deleteTaskItem(taskItem)
                        (binding.todoListRecyclerView.adapter as TaskItemAdapter).notifyItemRemoved(position)
                    }
                    .setNegativeButton("Cancel") { dialog, which ->
                        // Notify the adapter to reset the view at that position
                        (binding.todoListRecyclerView.adapter as TaskItemAdapter).notifyItemChanged(position)
                    }
                    .setOnCancelListener {
                        // Also handle the back button press to cancel
                        (binding.todoListRecyclerView.adapter as TaskItemAdapter).notifyItemChanged(position)
                    }
                    .show()
            }
        })
        itemTouchHelper.attachToRecyclerView(binding.todoListRecyclerView)
    }


    override fun editTaskItem(taskItem: TaskItem) {
        NewTaskSheet(taskItem).show(supportFragmentManager, "newTaskTag")
    }

    override fun completeTaskItem(taskItem: TaskItem) {
        taskViewModel.setCompleted(taskItem)
    }
    override fun deleteTaskItem(taskItem: TaskItem) {
        AlertDialog.Builder(this)
            .setTitle("Confirm Deletion")
            .setMessage("Are you sure you want to delete this task?")
            .setPositiveButton("Delete") { dialog, which ->
                taskViewModel.deleteTaskItem(taskItem)  // Ensure this matches the method in ViewModel
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
