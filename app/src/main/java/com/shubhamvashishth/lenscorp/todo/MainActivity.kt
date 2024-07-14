package com.shubhamvashishth.lenscorp.todo

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.shubhamvashishth.lenscorp.todo.ui.common.BottomBar
import com.shubhamvashishth.lenscorp.todo.ui.navigation.SetupNavGraph
import com.shubhamvashishth.lenscorp.todo.ui.theme.SmartTaskManagerTheme
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "task_notifications",
                "Task Notifications",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Channel for task notifications"
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        //sendTestNotification()

        //enableEdgeToEdge()
        setContent {
            SmartTaskManagerTheme {
               MainScreen()
            }
        }
    }
    private fun sendTestNotification() {
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "task_notifications"

        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Test Notification")
            .setContentText("This is a test notification to verify channel setup.")
            .setSmallIcon(R.drawable.ic_launcher_background) // Ensure you have this drawable resource
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }



}

private fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate()) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }
}

private fun showBiometricPrompt(context: Context) {
    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            // Handle error
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            // Handle success
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            // Handle failure

        }
    })

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Biometric Authentication")
        .setSubtitle("Log in using your biometric credential")
        .setNegativeButtonText("Use account password")
        .build()

    biometricPrompt.authenticate(promptInfo)
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    val context = LocalContext.current as FragmentActivity

    LaunchedEffect(Unit) {
        if (isBiometricAvailable(context)) {
            showBiometricPrompt(context)
        }
    }
    val navController = rememberNavController()
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController) },
        topBar = {
            TopAppBar(navController = navController)
        },
                floatingActionButton = {

                FloatingActionButton(
                    onClick = {
                        // Handle FAB click
                        navController.navigate("add")

                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }

        }

    ) {
            paddingValues ->
        // Apply padding manually
        Box(modifier = Modifier.padding(top = 50.dp)) {
            SetupNavGraph(navHostController = navController)
        }
    }
}

@Composable
fun TopAppBar(navController: NavHostController){
    var ok by remember{ mutableStateOf("") }
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .height(50.dp)
        .fillMaxWidth()){
        Column{
            Text(text = "ReadPin",  fontSize = 30.sp,
                fontWeight = FontWeight.Bold,)
            //  TextField(value = ok, onValueChange = { ok= it } )
        }
    }
}