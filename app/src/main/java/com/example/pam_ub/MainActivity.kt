package com.example.pam_ub

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText

data class TodoItem(val date: String, val task: String)

class MainActivity : AppCompatActivity() {
    private lateinit var todoAdapter: TodoAdapter
    private val todoList = mutableListOf(
        TodoItem("21 sep 2024", "speed run manga"),
        TodoItem("32 sep 2024", "makan nasgor"),
        TodoItem("2 okt 2024", "makan sama faroz"),
        TodoItem("4 okt 2024", "ayam")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val recyclerView: RecyclerView = findViewById(R.id.todoRecyclerView)
        todoAdapter = TodoAdapter(todoList)
        recyclerView.adapter = todoAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        val dateEditText: TextInputEditText = findViewById(R.id.dateEditText)
        val taskEditText: TextInputEditText = findViewById(R.id.taskEditText)
        val addButton: Button = findViewById(R.id.addButton)

        addButton.setOnClickListener {
            val date = dateEditText.text.toString()
            val task = taskEditText.text.toString()

            if (date.isNotEmpty() && task.isNotEmpty()) {
                val newTodoItem = TodoItem(date, task)
                todoAdapter.addItem(newTodoItem)
                recyclerView.scrollToPosition(todoList.size - 1)

                // Clear input fields
                dateEditText.text?.clear()
                taskEditText.text?.clear()
            } else {
                Toast.makeText(this, "Please fill both date and task", Toast.LENGTH_SHORT).show()
            }
        }
    }

    inner class TodoAdapter(private val todoList: MutableList<TodoItem>) :
        RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

        inner class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
            val taskTextView: TextView = itemView.findViewById(R.id.taskTextView)
            val deleteButton: ImageButton = itemView.findViewById(R.id.deleteButton)
        }


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_item, parent, false)
            return TodoViewHolder(itemView)
        }


        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            val currentItem = todoList[position]
            holder.dateTextView.text = currentItem.date
            holder.taskTextView.text = currentItem.task

            holder.deleteButton.setOnClickListener {
                deleteItem(position)
            }
        }

        override fun getItemCount() = todoList.size

        fun addItem(todoItem: TodoItem) {
            todoList.add(todoItem)
            notifyItemInserted(todoList.size - 1)
        }

        private fun deleteItem(position: Int) {
            if (position >= 0 && position < todoList.size) {
                todoList.removeAt(position)
                notifyItemRemoved(position)
            } else {
                Toast.makeText(this@MainActivity, "Invalid position", Toast.LENGTH_SHORT).show()
            }
        }

    }
}