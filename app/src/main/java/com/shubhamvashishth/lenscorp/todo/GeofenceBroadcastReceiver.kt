package com.shubhamvashishth.lenscorp.todo

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ok geo", "reached here")
        GeofenceTransitionsJobIntentService.enqueueWork(context, intent)
    }
}

