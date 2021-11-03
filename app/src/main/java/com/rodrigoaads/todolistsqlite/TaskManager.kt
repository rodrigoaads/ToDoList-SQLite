package com.rodrigoaads.todolistsqlite

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.rodrigoaads.todolistsqlite.Application.TaskApplication
import com.rodrigoaads.todolistsqlite.Model.TaskModel
import com.rodrigoaads.todolistsqlite.Model.TaskStatus
import kotlinx.android.synthetic.main.activity_task_manager.*

class TaskManager : AppCompatActivity() {

    private var idTask = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_manager)
        setupTask()

        buttonSave.setOnClickListener {
            validateTask()
        }

        buttonDelete.setOnClickListener {
            deleteTask()
        }

        switchStatus.setOnClickListener {
            when {
                switchStatus.isChecked -> {
                    textStatus.text = getString(R.string.done)
                }
                else -> {
                    textStatus.text = getString(R.string.waiting)
                }
            }
        }
    }

    private fun setupTask() {
        idTask = intent.getIntExtra("id", -1)
        if (idTask == -1) {
            textStatus.text = getString(R.string.waiting)
            buttonDelete.visibility = View.GONE
        } else {
            val task = TaskApplication.instance.helperDB?.loadTask(idTask) ?: mutableListOf()
            val setTask = task.getOrNull(0)
            editTextTextName.setText(setTask?.taskName)
            when (setTask?.taskStatus) {
                TaskStatus.DONE -> {
                    switchStatus.isChecked = true
                    textStatus.text = getString(R.string.done)
                }
                else -> {
                    switchStatus.isChecked = false
                    textStatus.text = getString(R.string.waiting)
                }
            }
        }
    }

    private fun deleteTask() {
        TaskApplication.instance.helperDB?.deleteTask(idTask)
        Toast.makeText(this, getString(R.string.delete_task), Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun saveTask() {
        if (idTask > -1) {
            when {
                switchStatus.isChecked -> {
                    TaskApplication.instance.helperDB?.updateTask(
                        TaskModel(
                            idTask,
                            editTextTextName.text.toString(),
                            TaskStatus.DONE
                        )
                    )
                }
                else -> {
                    TaskApplication.instance.helperDB?.updateTask(
                        TaskModel(
                            idTask,
                            editTextTextName.text.toString(),
                            TaskStatus.WAITING
                        )
                    )
                }
            }
            Toast.makeText(this, getString(R.string.update_task), Toast.LENGTH_SHORT).show()
        } else {
            when {
                switchStatus.isChecked -> {
                    TaskApplication.instance.helperDB?.saveTask(
                        TaskModel(
                            0,
                            editTextTextName.text.toString(),
                            TaskStatus.DONE
                        )
                    )
                }
                else -> {
                    TaskApplication.instance.helperDB?.saveTask(
                        TaskModel(
                            0,
                            editTextTextName.text.toString(),
                            TaskStatus.WAITING
                        )
                    )
                }
            }
        }
        finish()
    }

    fun validateTask() {
        if (editTextTextName.text.isBlank() || editTextTextName.text.isEmpty()) {
            Toast.makeText(this, getString(R.string.validate_string), Toast.LENGTH_SHORT).show()
        } else {
            saveTask()
        }
    }
}