package com.shubhamvashishth.lenscorp.todo.data.repository

import com.shubhamvashishth.lenscorp.todo.data.dao.TaskDao
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import javax.inject.Inject

class TaskRepository @Inject constructor( var taskDao: TaskDao) {

    suspend fun insert(task: TodoTask) {
        taskDao.insert(task)
    }

    suspend fun delete(task: TodoTask) {
        taskDao.delete(task)
    }

    suspend fun deleteById(id: Int) {
        taskDao.deleteById(id)
    }

    suspend fun getTaskById(id: Int): TodoTask {
        return taskDao.getTaskById(id)
    }

    suspend fun getAllTasks(): List<TodoTask> {
        return taskDao.getAllTasks()
    }

    suspend fun replaceTask(task: TodoTask) {
        taskDao.update(task)
    }



}