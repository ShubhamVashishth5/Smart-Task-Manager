package com.shubhamvashishth.lenscorp.todo.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.shubhamvashishth.lenscorp.todo.ui.homescreen.HomeScreen

fun NavGraphBuilder.homeNavGraph(
    navController: NavHostController
){
    navigation(startDestination = Screens.Home.route ,route = HOME_GRAPH_ROUTE)
    {
        composable(route=Screens.Home.route){
            HomeScreen(navController)
        }
    }

}