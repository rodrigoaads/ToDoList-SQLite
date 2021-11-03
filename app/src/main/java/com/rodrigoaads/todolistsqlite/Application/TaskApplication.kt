package com.rodrigoaads.todolistsqlite.Application

import android.app.Application
import com.rodrigoaads.todolistsqlite.Helper.HelperDB

class TaskApplication : Application() {

    var helperDB: HelperDB? = null
        private set

    companion object {
        lateinit var instance: TaskApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        helperDB = HelperDB(this)
    }
}