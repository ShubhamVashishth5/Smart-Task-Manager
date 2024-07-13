package com.shubhamvashishth.lenscorp.todo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

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
//        discoverNavGraph(navHostController)
//        userNavGraph(navHostController)
//        viewBookNavGraph(navHostController)
//        viewAuthorNavGraph(navHostController)
    }

}