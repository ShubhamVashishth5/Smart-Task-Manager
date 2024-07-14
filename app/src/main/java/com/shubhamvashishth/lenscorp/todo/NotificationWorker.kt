import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.maps.model.LatLng
import com.shubhamvashishth.lenscorp.todo.MainActivity
import com.shubhamvashishth.lenscorp.todo.R
import com.shubhamvashishth.lenscorp.todo.data.model.Priority
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
import com.shubhamvashishth.lenscorp.todo.data.repository.TaskRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class NotificationWorker (
    context: Context,
    workerParams: WorkerParameters,
) : Worker(context, workerParams) {

    @Inject
    lateinit var taskRepository : TaskRepository

    override fun doWork(): Result {
        // Retrieve the task ID from input data
        val taskId = inputData.getString("TASK_ID") ?: return Result.failure()

        // Fetch data from database
        val task = runBlocking {
            fetchTaskFromDatabase(taskId)
        } ?: return Result.failure()

        // Create and show notification
        showNotification(task)

        return Result.success()
    }

    private suspend fun fetchTaskFromDatabase(taskId: String): TodoTask? {
        return withContext(Dispatchers.IO) {
            taskRepository.getTaskById(taskId.toInt())
        }
    }

    private fun showNotification(task: TodoTask) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_notifications"

        // Create notification channel for API 26+
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, "Task Notifications", NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(task.title)
            .setContentText(task.description)
            .setSmallIcon(R.drawable.ic_launcher_background) // Replace with your notification icon
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(task.taskId.hashCode(), notification)
    }
}