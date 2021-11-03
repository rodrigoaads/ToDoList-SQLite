package com.rodrigoaads.todolistsqlite.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rodrigoaads.todolistsqlite.Model.TaskModel
import com.rodrigoaads.todolistsqlite.Model.TaskStatus
import com.rodrigoaads.todolistsqlite.R
import kotlinx.android.synthetic.main.item_task.view.*

class TaskAdapter(
    val list: List<TaskModel>,
    val onClick: ((Int) -> Unit),
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LayoutView(
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val task = list[position]
        holder.itemView.taskTitle.text = task.taskName

        when (task.taskStatus) {
            TaskStatus.DONE -> {
                holder.itemView.taskIcon.setImageResource(R.drawable.ic_baseline_check)
            }
            else -> {
                holder.itemView.taskIcon.setImageResource(R.drawable.ic_baseline_info)
            }
        }

        holder.itemView.setOnClickListener { onClick(task.taskId) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class LayoutView(itemView: View) : RecyclerView.ViewHolder(itemView) {}
}