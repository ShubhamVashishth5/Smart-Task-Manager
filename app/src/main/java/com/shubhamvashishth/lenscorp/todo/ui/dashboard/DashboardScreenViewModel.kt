package com.shubhamvashishth.lenscorp.todo.ui.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import com.shubhamvashishth.lenscorp.todo.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    var taskRepository: TaskRepository
):ViewModel() {

    private val _todoTasks = MutableStateFlow<List<TodoTask>>(emptyList())
    val todoTasks: StateFlow<List<TodoTask>> get() = _todoTasks

    init {
        loadTodoList()
    }

    fun loadTodoList() {
        viewModelScope.launch {
            val tasks = taskRepository.getAllTasks()
            _todoTasks.value = tasks
        }
    }
}