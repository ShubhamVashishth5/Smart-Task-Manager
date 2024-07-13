package com.shubhamvashishth.lenscorp.todo.ui.addtask

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamvashishth.lenscorp.todo.data.dao.TaskDao
import com.shubhamvashishth.lenscorp.todo.data.model.Priority
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import com.shubhamvashishth.lenscorp.todo.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditAddTaskViewModel @Inject constructor(
    var taskRepository: TaskRepository
) : ViewModel() {

    var todoTask = MutableLiveData<TodoTask?>()

    var saveTo: (TodoTask) -> Unit = { task ->

        viewModelScope.launch {
            taskRepository.insert(task)
        }

    }

    var edit: (TodoTask) -> Unit = { task ->

        viewModelScope.launch {
            taskRepository.replaceTask(task)
        }

    }

    fun loadTaskById(id: Int) {
        viewModelScope.launch {
            todoTask.postValue(taskRepository.getTaskById(id))
            Log.d("OK", id.toString()+todoTask.value.toString())
        }
    }

    fun deleteTodoTask(id: Int){
        viewModelScope.launch {
            taskRepository.deleteById(id)
        }
    }

}