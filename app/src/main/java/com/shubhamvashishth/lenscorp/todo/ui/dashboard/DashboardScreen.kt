package com.shubhamvashishth.lenscorp.todo.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.hd.charts.LineChartView
import com.hd.charts.PieChartView
import com.hd.charts.StackedBarChartView
import com.hd.charts.common.model.ChartDataSet
import com.hd.charts.common.model.MultiChartDataSet
import com.hd.charts.style.ChartViewDefaults
import com.hd.charts.style.ChartViewStyle
import com.hd.charts.style.LineChartDefaults
import com.hd.charts.style.PieChartDefaults
import com.hd.charts.style.PieChartStyle
import com.hd.charts.style.StackedBarChartDefaults
import com.shubhamvashishth.lenscorp.todo.data.model.Priority
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import com.shubhamvashishth.lenscorp.todo.ui.theme.ColorPalette


@Composable
fun DashboardScreen(viewModel: DashboardScreenViewModel = hiltViewModel()) {
    val tasks by viewModel.todoTasks.collectAsState()

    // Example insights
    val completedTasks = tasks.count { it.isCompleted }
    val pendingTasks = tasks.size - completedTasks
    val highPriorityTasks = tasks.count { it.taskPriority == Priority.HIGH }
    val mediumPriorityTasks = tasks.count { it.taskPriority == Priority.MEDIUM }
    val lowPriorityTasks = tasks.count { it.taskPriority == Priority.LOW }

    LazyColumn(modifier = Modifier.padding(bottom = 40.dp)) {
        item {  AddCustomPieChart(tasks)}
        item { LazyRow {
            item {  AddCustomStackedBarChart()}
            item {  AddCustomMultiLineChart() }

        } }

    }


}

@Composable
private fun AddCustomPieChart(taskList: List<TodoTask>) {

    val low = taskList.count {
        it.taskPriority==Priority.LOW
    }.toFloat()
    val medium = taskList.count {
        it.taskPriority==Priority.MEDIUM
    }.toFloat()
    val high = taskList.count {
        it.taskPriority==Priority.HIGH
    }.toFloat()

    val pieColors = listOf(
        Color.Blue,
        Color.Red,
        Color.Green,
        )

    val style = PieChartDefaults.style(
        borderColor = Color.White,
        donutPercentage = 40f,
        borderWidth = 6f,
        pieColors = pieColors,
    )



    val dataSet = ChartDataSet(
        items = listOf(low, medium, high),
        title = "Remaining task by priority",
        postfix = ""
    )

    PieChartView(dataSet = dataSet, style = style)
}

@Composable
private fun AddCustomStackedBarChart() {
    val colors = listOf(
        Color.Blue,
        Color.Red,
        Color.Green,
    )
    val style =  StackedBarChartDefaults.style(
        barColors = colors,
        chartViewStyle = ChartViewDemoStyle.custom(width = 200.dp)
    )

    val items = listOf(
        "Cherry St." to listOf(8261.68f, 8810.34f, 30000.57f),
        "Strawberry Mall" to listOf(8261.68f, 8810.34f, 30000.57f),
        "Lime Av." to listOf(1500.87f, 2765.58f, 33245.81f),
        "Apple Rd." to listOf(5444.87f, 233.58f, 67544.81f)
    )

    val dataSet = MultiChartDataSet(
        items = items,
        prefix = "$",
        categories = listOf("Jan", "Feb", "Mar"),
        title = "Ok"
    )

    StackedBarChartView(dataSet = dataSet, style = style)
}

object ChartViewDemoStyle {

    // We are changing the default ChartView style just for demo purposes
    @Composable
    fun custom(width: Dp = Dp.Infinity): ChartViewStyle {
        return ChartViewDefaults.style(
            width = width,
            )
    }
}

@Composable
private fun AddCustomMultiLineChart() {
    val lineColors = listOf(ColorPalette.DataColor.navyBlue, ColorPalette.DataColor.darkBlue, ColorPalette.DataColor.deepPurple, ColorPalette.DataColor.magenta)
    val style = LineChartDefaults.style(
        lineColors = lineColors,
        dragPointVisible = false,
        pointVisible = true,
        pointColor = ColorPalette.DataColor.magenta,
        dragPointColor = ColorPalette.DataColor.deepPurple,
        chartViewStyle = ChartViewDemoStyle.custom(200.dp)
    )

    val items = listOf(
        "Cherry St." to listOf(26000.68f, 28000.34f, 32000.57f, 45000.57f),
        "Strawberry Mall" to listOf(15261.68f, 17810.34f, 40000.57f, 85000f),
        "Lime Av." to listOf(4000.87f, 5000.58f, 30245.81f, 135000.58f),
        "Apple Rd." to listOf(1000.87f, 9000.58f, 16544.81f, 100444.87f)
    )

    val dataSet = MultiChartDataSet(
        items = items,
        prefix = "$",
        categories = listOf("Jan", "Feb", "Mar", "Apr"),
        title = "Titel"
    )

    LineChartView(dataSet = dataSet, style = style)
}
