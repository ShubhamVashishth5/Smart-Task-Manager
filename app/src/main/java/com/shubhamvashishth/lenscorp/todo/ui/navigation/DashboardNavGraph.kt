package com.shubhamvashishth.lenscorp.todo.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shubhamvashishth.lenscorp.todo.ui.dashboard.DashboardScreen

fun NavGraphBuilder.dashboardNavGraph(
    navController: NavHostController
){
    navigation(startDestination = Screens.Dashboard.route ,route = DASHBOARD_GRAPH_ROUTE)
    {
        composable(route= Screens.Dashboard.route){
            DashboardScreen()
        }
    }

}