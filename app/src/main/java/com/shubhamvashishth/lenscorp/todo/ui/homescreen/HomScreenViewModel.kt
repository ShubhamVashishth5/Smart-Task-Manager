package com.shubhamvashishth.lenscorp.todo.ui.homescreen

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamvashishth.lenscorp.todo.data.dao.TaskDao
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import com.shubhamvashishth.lenscorp.todo.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class HomScreenViewModel @Inject constructor(var taskRepository: TaskRepository) : ViewModel() {

    val taskList = MutableLiveData<List<TodoTask>>();
    val unfiltereTask = mutableListOf<TodoTask>();
    private val isLoading = AtomicBoolean(false) // AtomicBoolean to prevent re-entrance
    private var isLoaded = false

    private val mutex = Mutex()


    fun loadTodoList() {
        if (isLoading.compareAndSet(false, true)) {
            viewModelScope.launch {
                mutex.withLock {
                    try {
                        if (!isLoaded) { // Check if already loaded
                            val tasks = taskRepository.getAllTasks()
                            taskList.postValue(tasks.sortedByDescending {
                                it.dueDate
                            })

                            if (unfiltereTask.isEmpty() || unfiltereTask.size<tasks.size) {
                                unfiltereTask.clear()
                                unfiltereTask.addAll(tasks.sortedByDescending {it.dueDate  })
                            }

                        }
                    } finally {
                        isLoading.set(false)
                    }
                }
            }
        }
    }

    fun deleteTodoTask(task: TodoTask) {
        viewModelScope.launch {
            taskRepository.deleteById(task.taskId)
            taskRepository.insert(task.apply {
                this.isCompleted = !this.isCompleted
            })

        }
    }

    var statusFilter = StatusOption.All
    var priorityFilter = PriorityOption.All
    var searchString = ""

    fun filterList(query: String) {

        searchString = query
        taskList.value = getFilteredTasks()

    }

    fun priorityAndStatusFilter(filterOption: FilterOption) {

        when (filterOption) {
            is FilterOption.Status -> statusFilter = filterOption.option
            is FilterOption.Priority -> priorityFilter = filterOption.option
        }
        taskList.value = getFilteredTasks().sortedByDescending { it.dueDate }

    }

    fun getFilteredTasks(): List<TodoTask> {
        return unfiltereTask.filter {
            (statusFilter == StatusOption.All || it.isCompleted == getStatusBoolean(statusFilter)) && (priorityFilter == PriorityOption.All || it.taskPriority.ordinal == getPriorityInteger(
                priorityFilter
            ))
                    && (it.title.contains(
                searchString,
                ignoreCase = true
            ) || it.description.contains(searchString, ignoreCase = true)
                    )
        }
    }

    private fun getStatusBoolean(statusOption: StatusOption): Boolean {
        return statusOption != StatusOption.Incomplete
    }

    private fun getPriorityInteger(priorityOption: PriorityOption): Int {
        return priorityOption.ordinal
    }

}