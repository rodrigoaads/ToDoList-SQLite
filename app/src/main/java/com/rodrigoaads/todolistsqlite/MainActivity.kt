package com.rodrigoaads.todolistsqlite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.rodrigoaads.todolistsqlite.Adapter.TaskAdapter
import com.rodrigoaads.todolistsqlite.Application.TaskApplication
import com.rodrigoaads.todolistsqlite.Model.TaskModel
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonAdd.setOnClickListener {
            val intent = Intent(this, TaskManager::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerView() {
        var list: List<TaskModel> = mutableListOf()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            try {
                list = TaskApplication.instance.helperDB?.setupList() ?: mutableListOf()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            adapter = TaskAdapter(list) { onClickRecyclerView(it) }
        }
    }

    fun onClickRecyclerView(id: Int) {
        val intent = Intent(this, TaskManager::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }
}