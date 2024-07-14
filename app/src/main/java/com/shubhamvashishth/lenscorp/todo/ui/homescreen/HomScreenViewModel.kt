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
import javax.inject.Inject

@HiltViewModel
class HomScreenViewModel @Inject constructor(var taskRepository: TaskRepository) : ViewModel() {

    val taskList = MutableLiveData<List<TodoTask>>();
    val unfiltereTask = mutableListOf<TodoTask>();


    fun loadTodoList() {
        viewModelScope.launch {
            taskList.postValue(taskRepository.getAllTasks())
            unfiltereTask.addAll(taskRepository.getAllTasks())
        }
    }

    fun deleteTodoTask(id: Int) {
        viewModelScope.launch {
            taskRepository.deleteById(id)

        }
    }

    var statusFilter = StatusOption.All
    var priorityFilter = PriorityOption.All
    var searchString = ""

    fun filterList(query: String) {
        searchString = query
        taskList.value = getFilteredTasks()
//            unfiltereTask.filter {
//            it.title.contains(query, ignoreCase = true) || it.description.contains(
//                query,
//                ignoreCase = true
//            )
//        }
//        if (query == "") {
//            taskList.postValue(unfiltereTask)
//        }
    }

    fun priorityAndStatusFilter(filterOption: FilterOption) {

        when (filterOption) {
            is FilterOption.Status -> statusFilter = filterOption.option
            is FilterOption.Priority -> priorityFilter = filterOption.option
        }
        taskList.value = getFilteredTasks()

//            unfiltereTask.filter {
//            (statusFilter == StatusOption.All || it.isCompleted == getStatusBoolean(statusFilter)) && (priorityFilter == PriorityOption.All || it.taskPriority.ordinal == getPriorityInteger(
//                priorityFilter
//            ))
//        }

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
        return statusOption == StatusOption.Incomplete
    }

    private fun getPriorityInteger(priorityOption: PriorityOption): Int {
        return priorityOption.ordinal
    }

}