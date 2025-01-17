import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import com.hd.charts.style.ChartViewDefaults
import com.hd.charts.style.ChartViewStyle
import com.shubhamvashishth.lenscorp.todo.MainApplication
import com.shubhamvashishth.lenscorp.todo.data.model.Priority
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Format date from UNIX format to Readable format
 * @return convertedString
 */
fun formatDate(date: Date): String {
    val outputFormat = SimpleDateFormat("EEEE, MMM dd yyyy, h:mm a", Locale.ENGLISH)
    return outputFormat.format(date)
}

/**
 * Get completed tasks in last 4 months, with categories for priorities and their count in each of them
 * @param taskList : List of tasks
 * @return Map<String, Map<Priority, Float>>
 *
 */
fun getCompletedTasksByMonth(taskList: List<TodoTask>): Map<String, Map<Priority, Float>> {
    val calendar = Calendar.getInstance()
    val today = calendar.time
    val months = mutableListOf<String>()

    for (i in 0 until 4) {
        calendar.time = today
        calendar.add(Calendar.MONTH, -i)
        val monthName = calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
        months.add(monthName ?: "")
    }

    val result = mutableMapOf<String, MutableMap<Priority, Float>>()
    months.forEach { month ->
        result[month] = mutableMapOf(
            Priority.LOW to 0f,
            Priority.MEDIUM to 0f,
            Priority.HIGH to 0f
        )
    }

    taskList.forEach { task ->
        if (task.isCompleted) {
            val taskDate = task.dueDate
            calendar.time = taskDate
            val taskMonth =
                calendar.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault()) ?: ""

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

/**
 * Style object for charts
 */
object ChartViewDemoStyle {

    @Composable
    fun custom(width: Dp = Dp.Infinity): ChartViewStyle {
        return ChartViewDefaults.style(
            width = width,
        )
    }
}

/**
 * Function to return the tasks completed in last 6 weeks
 * @param tasks: List of tasks
 * @return List<Int> with task completed in each of last 6 weeks
 */
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

fun shouldShowFab(currentDestination: String?): Boolean {
    val fabVisibleDestinations = listOf("home_screen") // Add paths where FAB should be visible
    return currentDestination in fabVisibleDestinations
}