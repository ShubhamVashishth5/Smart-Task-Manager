package com.shubhamvashishth.lenscorp.todo.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shubhamvashishth.lenscorp.todo.ui.addtask.AddEditTaskScreen
import com.shubhamvashishth.lenscorp.todo.ui.common.TodoTaskForm


fun NavGraphBuilder.settingsNavGraph(
    navController: NavHostController
){
    navigation(startDestination = Screens.Settings.route ,route = SETTINGS_PROFILE_ROUTE)
    {
        composable(route= Screens.Settings.route){
            AddEditTaskScreen(navController = navController)
        }
    }

}