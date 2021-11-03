package com.rodrigoaads.todolistsqlite.Helper

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.rodrigoaads.todolistsqlite.Model.TaskModel
import com.rodrigoaads.todolistsqlite.Model.TaskStatus

class HelperDB(context: Context?) : SQLiteOpenHelper(context, DATABASE_NAME, null, VERSION) {

    companion object {
        private val DATABASE_NAME = "taskrepository.db"
        private val VERSION = 1
    }

    val TABLE_NAME = "tasks"
    val COLUMN_ID = "id"
    val COLUMN_NAME = "name"
    val COLUMN_STATUS = "status"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TABLE = "CREATE TABLE $TABLE_NAME(" +
            "$COLUMN_ID INTEGER NOT NULL," +
            "$COLUMN_NAME TEXT NOT NULL," +
            "$COLUMN_STATUS TEXT NOT NULL," +
            "" +
            "PRIMARY KEY($COLUMN_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(p0: SQLiteDatabase?) {
        p0?.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {
        if (p1 != p2) {
            p0?.execSQL(DROP_TABLE)
        }
        onCreate(p0)
    }

    fun setupList(): List<TaskModel> {
        val db = readableDatabase ?: return mutableListOf()
        val list = mutableListOf<TaskModel>()
        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME", null)
        if (cursor == null) {
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()) {
            when (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))) {
                "DONE" -> {
                    val task = TaskModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        TaskStatus.DONE
                    )
                    list.add(task)
                }
                else -> {
                    val task = TaskModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        TaskStatus.WAITING
                    )
                    list.add(task)
                }
            }
        }
        db.close()
        return list
    }

    fun saveTask(taskModel: TaskModel) {
        val db = writableDatabase ?: return
        val content = ContentValues()
        content.put(COLUMN_NAME, taskModel.taskName)
        content.put(COLUMN_STATUS, taskModel.taskStatus.toString())
        db.insert(TABLE_NAME, null, content)
        db.close()
    }

    fun loadTask(id: Int): List<TaskModel> {
        val db = readableDatabase ?: return mutableListOf()
        val list = mutableListOf<TaskModel>()
        val where = "$COLUMN_ID = ?"
        val arg = arrayOf("$id")
        val cursor = db.query(TABLE_NAME, null, where, arg, null, null, null)
        if (cursor == null) {
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()) {
            when (cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_STATUS))) {
                "DONE" -> {
                    val task = TaskModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        TaskStatus.DONE
                    )
                    list.add(task)
                }
                else -> {
                    val task = TaskModel(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME)),
                        TaskStatus.WAITING
                    )
                    list.add(task)
                }
            }
        }
        db.close()
        return list
    }

    fun deleteTask(id: Int) {
        val db = writableDatabase ?: return
        val where = "$COLUMN_ID = ?"
        val arg = arrayOf("$id")
        db.delete(TABLE_NAME, where, arg)
        db.close()
    }

    fun updateTask(taskModel: TaskModel) {
        val db = writableDatabase ?: return
        val where = "$COLUMN_ID = ?"
        val arg = arrayOf("${taskModel.taskId}")
        val content = ContentValues()
        content.put(COLUMN_NAME, taskModel.taskName)
        content.put(COLUMN_STATUS, taskModel.taskStatus.toString())
        db.update(TABLE_NAME, content, where, arg)
        db.close()
    }
}