package com.shubhamvashishth.lenscorp.todo.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask

@Dao
interface TaskDao {

    @Insert
    suspend fun insert(task: TodoTask)

    @Delete
    suspend  fun delete(task: TodoTask)

    @Query("DELETE from TodoTask where taskId= :id")
    suspend  fun deleteById(id: Int)

    @Query("SELECT * FROM TodoTask")
    suspend fun getAllTasks(): List<TodoTask>

}