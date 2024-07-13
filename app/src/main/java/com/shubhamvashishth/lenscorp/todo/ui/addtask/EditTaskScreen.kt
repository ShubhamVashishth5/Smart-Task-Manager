package com.shubhamvashishth.lenscorp.todo.ui.addtask

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shubhamvashishth.lenscorp.todo.ui.common.TodoTaskForm

@Composable
fun EditTaskScreen(navController: NavController, id: Int){

    var editAddTaskViewModel : EditAddTaskViewModel = hiltViewModel();


    val task = editAddTaskViewModel.todoTask.observeAsState()
    if (task.value==null) {  editAddTaskViewModel.loadTaskById(id)}


    TodoTaskForm(task.value, onSave = { task->
        Log.d("ok",task.taskId.toString())
        editAddTaskViewModel.deleteTodoTask(id)
        editAddTaskViewModel.saveTo(task)
     //   editAddTaskViewModel.edit(task)
        navController.popBackStack()

    }, onCancel = {
        navController.popBackStack()
    })

}