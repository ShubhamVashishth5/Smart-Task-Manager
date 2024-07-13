package com.shubhamvashishth.lenscorp.todo.ui.addtask

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.shubhamvashishth.lenscorp.todo.ui.common.TodoTaskForm

@Composable
fun AddEditTaskScreen(navController: NavController){

    var editAddTaskViewModel : EditAddTaskViewModel = hiltViewModel();

    TodoTaskForm(onSave = editAddTaskViewModel.saveTo, onCancel = {})

}