package com.shubhamvashishth.lenscorp.todo.ui.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


@Composable
fun DashboardScreen(viewModel: DashboardScreenViewModel = hiltViewModel()) {
    val tasks by viewModel.todoTasks.collectAsState()

    // Example insights
    val completedTasks = tasks.count { it.isCompleted }
    val pendingTasks = tasks.size - completedTasks
    val highPriorityTasks = tasks.count { it.taskPriority == Priority.HIGH }
    val mediumPriorityTasks = tasks.count { it.taskPriority == Priority.MEDIUM }
    val lowPriorityTasks = tasks.count { it.taskPriority == Priority.LOW }

    if (tasks.size>0){
    LazyColumn(modifier = Modifier.padding(bottom = 40.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        item { PriorityLegend() } // Add the legend at the top

        item {  AddCustomPieChart(tasks)}
        item { LazyRow {
            item {  AddCustomStackedBarChart(tasks)}
//            item {  AddCustomMultiLineChart(tasks) }
            item {
                AddCustomLineChart(tasks)
            }

        } }

    }}


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
        ColorPalette.DataColor.darkBlue,
        ColorPalette.DataColor.magenta,
        ColorPalette.DataColor.orange,
        )

    val style = PieChartDefaults.style(
        borderColor = Color.White,
        donutPercentage = 40f,
        borderWidth = 6f,
        pieColors = pieColors,
        chartViewStyle = ChartViewDemoStyle.custom(width = (LocalConfiguration.current.screenWidthDp/1.2).dp)
    )



    val dataSet = ChartDataSet(
        items = listOf(low, medium, high),
        title = "Remaining task by priority",
        postfix = " tasks"
    )

    PieChartView(dataSet = dataSet, style = style)
}

@Composable
fun PriorityLegend() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LegendItem("Low", ColorPalette.DataColor.darkBlue)
        LegendItem("Medium", ColorPalette.DataColor.magenta)
        LegendItem("High", ColorPalette.DataColor.orange)
    }
}

@Composable
fun LegendItem(label: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = label)
    }
}


@Composable
private fun AddCustomStackedBarChart(taskList: List<TodoTask>) {
    val colors = listOf(
        ColorPalette.DataColor.darkBlue,
        ColorPalette.DataColor.magenta,
        ColorPalette.DataColor.orange,
    )

    val style = StackedBarChartDefaults.style(
        barColors = colors,
        chartViewStyle = ChartViewDemoStyle.custom(width = (LocalConfiguration.current.screenWidthDp / 2.4).dp)
    )

    // Get data for the last 4 months
    val monthData = getCompletedTasksByMonth(taskList)

    // Prepare dataset for the chart
    val items = monthData.map { (month, priorities) ->
        month to listOf(
            priorities[Priority.LOW] ?: 0f,
            priorities[Priority.MEDIUM] ?: 0f,
            priorities[Priority.HIGH] ?: 0f
        )
    }


        val items2 = listOf(
            "Cherry St." to listOf(8261.68f, 8810.34f, 30000.57f),
            "Strawberry Mall" to listOf(8261.68f, 8810.34f, 30000.57f),
            "Lime Av." to listOf(1500.87f, 2765.58f, 33245.81f),
            "Apple Rd." to listOf(5444.87f, 233.58f, 67544.81f)
        )

    Log.d("ok",items2.toString())
    Log.d("ok",items.toString())

    val dataSet = MultiChartDataSet(
        items = items,
        prefix = "",
        categories = listOf("low", "medium", "high"),
        title = "Task Completion by Priority"
    )

    StackedBarChartView(dataSet = dataSet, style = style)
}

private fun getCompletedTasksByMonth(taskList: List<TodoTask>): Map<String, Map<Priority, Float>> {
    val calendar = Calendar.getInstance()
    val today = calendar.time
    val months = mutableListOf<String>()

    // Calculate the months for the last 4 months
    for (i in 0 until 4) {
        calendar.time = today
        calendar.add(Calendar.MONTH, -i)
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        months.add(monthName ?: "")
    }

    // Initialize result map
    val result = mutableMapOf<String, MutableMap<Priority, Float>>()
    months.forEach { month ->
        result[month] = mutableMapOf(
            Priority.LOW to 0f,
            Priority.MEDIUM to 0f,
            Priority.HIGH to 0f
        )
    }

    // Process the tasks
    taskList.forEach { task ->
        if (task.isCompleted) {
            val taskDate = task.dueDate
            calendar.time = taskDate
            val taskMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) ?: ""

            // Check if the task's month is within the last 4 months
            if (result.containsKey(taskMonth)) {
                result[taskMonth]?.let { priorities ->
                    val priorityCount = priorities[task.taskPriority] ?: 0f
                    priorities[task.taskPriority] = priorityCount + 1f
                }
            }
        }
    }

    return result
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
private fun AddCustomMultiLineChart(tasks: List<TodoTask>) {
    // Aggregating tasks by month and completion status
    val calendar = Calendar.getInstance()
    val monthlyCompletionData = tasks
        .filter { it.isCompleted } // Consider only completed tasks
        .groupBy { task ->
            calendar.apply { time = task.dueDate }
            "${calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())} ${calendar.get(Calendar.YEAR)}"
        }
        .mapValues { (_, tasksInMonth) -> tasksInMonth.size.toFloat() } // Count of tasks

    val months = monthlyCompletionData.keys.sorted()
    val counts = months.map { month -> monthlyCompletionData[month] ?: 0f }

    // Create dataset for chart
    val items = listOf(
        "Completed Tasks" to counts
    )

    val lineColors = listOf(
        ColorPalette.DataColor.orange // Color for completed tasks
    )

    val style = LineChartDefaults.style(
        lineColors = lineColors,
        dragPointVisible = false,
        pointVisible = true,
        pointColor = ColorPalette.DataColor.orange,
        dragPointColor = ColorPalette.DataColor.deepPurple,
        chartViewStyle = ChartViewDemoStyle.custom(width = (LocalConfiguration.current.screenWidthDp / 2.4).dp)
    )

    val dataSet = MultiChartDataSet(
        items = items,
        prefix = "", // No currency symbol for task counts
        categories = months,
        title = "Completed Tasks Over Time"
    )

    LineChartView(dataSet = dataSet, style = style)
}



@Composable
fun AddDefaultLineChart(tasks: List<TodoTask>) {
    val calendar = Calendar.getInstance()
    val taskCountByWeek = IntArray(6)

    tasks.forEach { task ->
        if (task.isCompleted) {
            calendar.time = task.dueDate
            val weeksAgo = 6 - ((calendar.get(Calendar.WEEK_OF_YEAR) - Calendar.getInstance().get(Calendar.WEEK_OF_YEAR)) % 52)
            if (weeksAgo in 0..5) {
                taskCountByWeek[weeksAgo]++
            }
        }
    }

    val dataSet = ChartDataSet(
        items = taskCountByWeek.map { it.toFloat() },
        title = "Task completed in last 6 weeks"
    )

    LineChartView(dataSet = dataSet)
}

@Composable
private fun AddCustomLineChart(tasks: List<TodoTask>) {
    val style = LineChartDefaults.style(
        lineColor = ColorPalette.DataColor.deepPurple,
        pointColor = ColorPalette.DataColor.magenta,
        pointSize = 9f,
        bezier = false,
        dragPointColor = ColorPalette.DataColor.deepPurple,
        dragPointVisible = false,
        dragPointSize = 8f,
        dragActivePointSize = 15f,
        chartViewStyle = ChartViewDemoStyle.custom(width = 200.dp)
    )
    val taskCountByWeek = getCompletedTasksCountPerWeek(tasks)

    val dataSet = ChartDataSet(
        items = taskCountByWeek.map { it.toFloat() },
        title = "Task completed in last 6 weeks"
    )

    LineChartView(dataSet = dataSet, style = style)
}


fun getCompletedTasksCountPerWeek(tasks: List<TodoTask>): List<Int> {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.WEEK_OF_YEAR, -6)
    val sixWeeksAgo = calendar.time

    val currentCalendar = Calendar.getInstance()
    val currentYear = currentCalendar.get(Calendar.YEAR)
    val currentWeek = currentCalendar.get(Calendar.WEEK_OF_YEAR)

    val counts = IntArray(6)

    for (task in tasks) {
        if (task.isCompleted && task.dueDate.after(sixWeeksAgo)) {
            val taskCalendar = Calendar.getInstance().apply { time = task.dueDate }
            val taskYear = taskCalendar.get(Calendar.YEAR)
            val taskWeek = taskCalendar.get(Calendar.WEEK_OF_YEAR)

            val weekDiff = (currentYear * 52 + currentWeek) - (taskYear * 52 + taskWeek)
            if (weekDiff in 0..5) {
                counts[5 - weekDiff]++
            }
        }
    }

    return counts.toList()
}