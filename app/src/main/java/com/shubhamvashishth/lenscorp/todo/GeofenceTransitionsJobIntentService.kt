package com.shubhamvashishth.lenscorp.todo

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.JobIntentService
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceTransitionsJobIntentService : JobIntentService() {

    companion object {
        private const val JOB_ID = 573

        fun enqueueWork(context: Context, work: Intent) {
            enqueueWork(context, GeofenceTransitionsJobIntentService::class.java, JOB_ID, work)
        }
    }

    override fun onHandleWork(intent: Intent) {
        sendNotification()

        if (GeofencingEvent.fromIntent(intent)?.geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            GeofencingEvent.fromIntent(intent)?.geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            // Handle the geofence transition and send a notification.
            sendNotification()
        }
    }

    private fun sendNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = "geofence_channel"
            val channelName = "Geofence Notifications"
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            channel.description = "Channel for geofence notifications"
            notificationManager.createNotificationChannel(channel)
        }

        val notificationBuilder = NotificationCompat.Builder(this, "geofence_channel")
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Task location Alert")
            .setContentText("You have reached a task location. Click here to open the app")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        notificationManager.notify(0, notificationBuilder.build())
    }
}
