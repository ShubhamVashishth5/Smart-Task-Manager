package com.shubhamvashishth.lenscorp.todo.ui.homescreen

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
class HomScreenViewModel @Inject constructor(var taskRepository: TaskRepository): ViewModel() {

    val taskList= MutableLiveData<List<TodoTask>>() ;

    fun loadTodoList() {
        viewModelScope.launch {
            taskList.postValue( taskRepository.getAllTasks())
        }
    }

    fun deleteTodoTask(id: Int){
        viewModelScope.launch {
            taskRepository.deleteById(id)
//            taskList.value = taskList.value?.filter {
//                it.taskId != id
//            }
            //loadTodoList()
        }
    }

}