package com.shubhamvashishth.lenscorp.todo.ui.addtask

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shubhamvashishth.lenscorp.todo.data.dao.TaskDao
import com.shubhamvashishth.lenscorp.todo.data.model.Priority
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class EditAddTaskViewModel @Inject constructor(
    taskDao: TaskDao
) : ViewModel() {

    var todoTask = MutableStateFlow<TodoTask?>(TodoTask(
        Priority.HIGH,
        "Title 1", Date(), "Desc", false, "Location"));

    var saveTo: (TodoTask) -> Unit = {
        task->

        viewModelScope.launch {
            Log.d("OK", "inserting")
            taskDao.insert(task)
            //todoTask.value?.let { taskDao.insert(it) }
        }


    }

}