package com.shubhamvashishth.lenscorp.todo.ui.navigation

sealed class Screens(val route: String) {
    object Home : Screens("home_screen")
    object Dashboard : Screens("dashboard_screen")
    object Settings : Screens("settings_screen")
    object EditTask : Screens("task/edit/{task_id}")
    object AddTask : Screens("task/add")
}