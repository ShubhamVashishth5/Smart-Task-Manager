package com.shubhamvashishth.lenscorp.todo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.shubhamvashishth.lenscorp.todo.data.dao.TaskDao
import com.shubhamvashishth.lenscorp.todo.data.model.Converters
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask

@Database(entities = [TodoTask::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao



}