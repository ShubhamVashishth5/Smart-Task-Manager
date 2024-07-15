package com.shubhamvashishth.lenscorp.todo.ui.dashboard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
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
import com.hd.charts.style.StackedBarChartDefaults
import com.shubhamvashishth.lenscorp.todo.data.model.Priority
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import com.shubhamvashishth.lenscorp.todo.ui.theme.ColorPalette
import getCompletedTasksByMonth
import getCompletedTasksCountPerWeek
import java.util.Calendar
import java.util.Locale


@Composable
fun DashboardScreen(viewModel: DashboardScreenViewModel = hiltViewModel()) {
    val tasks by viewModel.todoTasks.collectAsState()


    if (tasks.size > 0) {
        LazyColumn(
            modifier = Modifier.padding(bottom = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            item { PriorityLegend() } // Add the legend at the top

            item { AddCustomPieChart(tasks) }
            item {
                LazyRow {
                    item { AddCustomStackedBarChart(tasks) }
                    item {
                        AddCustomLineChart(tasks)
                    }

                }
            }

        }
    }else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Add some tasks to see some graphs here",
                style = MaterialTheme.typography.body1,
                textAlign = TextAlign.Center
            )
        }
    }


}

@Composable
private fun AddCustomPieChart(taskList: List<TodoTask>) {

    val low = taskList.count {
        it.taskPriority == Priority.LOW && !it.isCompleted
    }.toFloat()
    val medium = taskList.count {
        it.taskPriority == Priority.MEDIUM && !it.isCompleted
    }.toFloat()
    val high = taskList.count {
        it.taskPriority == Priority.HIGH && !it.isCompleted
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
        chartViewStyle = ChartViewDemoStyle.custom(width = (LocalConfiguration.current.screenWidthDp / 1.2).dp)
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

    val monthData = getCompletedTasksByMonth(taskList)

    val items = monthData.map { (month, priorities) ->
        month to listOf(
            priorities[Priority.LOW] ?: 0f,
            priorities[Priority.MEDIUM] ?: 0f,
            priorities[Priority.HIGH] ?: 0f
        )
    }


    val dataSet = MultiChartDataSet(
        items = items,
        prefix = "",
        categories = listOf("low", "medium", "high"),
        title = "Task Completion by Priority"
    )

    StackedBarChartView(dataSet = dataSet, style = style)
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


