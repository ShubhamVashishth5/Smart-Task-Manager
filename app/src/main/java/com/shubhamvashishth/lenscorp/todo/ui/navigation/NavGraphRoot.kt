package com.shubhamvashishth.lenscorp.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.shubhamvashishth.lenscorp.todo.ui.addtask.AddEditTaskScreen
import com.shubhamvashishth.lenscorp.todo.ui.addtask.EditTaskScreen

@Composable
fun SetupNavGraph(
    navHostController: NavHostController
){
    NavHost(navController = navHostController,
        startDestination = HOME_GRAPH_ROUTE,
        route = ROOT_GRAPH_ROUTE
    ){
        homeNavGraph(navHostController)
        dashboardNavGraph(navHostController)
        settingsNavGraph(navHostController)
        composable("add") { AddEditTaskScreen(navHostController) }
        composable("edit/{id}") {
                backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull()?:0
            EditTaskScreen(navHostController, id) }

//        discoverNavGraph(navHostController)
//        userNavGraph(navHostController)
//        viewBookNavGraph(navHostController)
//        viewAuthorNavGraph(navHostController)
    }

}