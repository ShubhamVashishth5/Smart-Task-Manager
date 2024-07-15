package com.shubhamvashishth.lenscorp.todo

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.shubhamvashishth.lenscorp.todo.ui.common.BottomBar
import com.shubhamvashishth.lenscorp.todo.ui.navigation.SetupNavGraph
import com.shubhamvashishth.lenscorp.todo.ui.theme.SmartTaskManagerTheme
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


        setContent {
            SmartTaskManagerTheme {
               MainScreen()
            }
        }
    }



}

private fun isBiometricAvailable(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    return when (biometricManager.canAuthenticate()) {
        BiometricManager.BIOMETRIC_SUCCESS -> true
        else -> false
    }
}

private fun showBiometricPrompt(context: Context, function: () -> Unit) {
    val executor = ContextCompat.getMainExecutor(context)

    val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor, object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            context.finishAffinity()

            // Handle error
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            function.invoke()
            // Handle success
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            context.finishAffinity()
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
    val biometricEnabled = remember { mutableStateOf(false) }


    var isAuthenticated by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        val sharedPreferences = context.getSharedPreferences("settings_prefs", Context.MODE_PRIVATE)
        biometricEnabled.value = sharedPreferences.getBoolean("biometric_enabled", false)

        if(biometricEnabled.value== false){
            isAuthenticated=true
        }
        else if (isBiometricAvailable(context)) {
            showBiometricPrompt(context) { isAuthenticated = true }
        }
    }
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination?.route
    Log.d("ok",currentDestination.toString())

if(isAuthenticated){
    Scaffold(
        bottomBar = {
            BottomBar(navController = navController) },

                floatingActionButton = {
                    if (shouldShowFab(currentDestination)) {
                FloatingActionButton(
                    onClick = {
                        // Handle FAB click
                        navController.navigate("add")

                    },
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Icon(Icons.Filled.Add, contentDescription = "Add Task")
                }}

        }

    ) {
            paddingValues ->
        // Apply padding manually
        Box(modifier = Modifier.padding(bottom = 40.dp)) {
            SetupNavGraph(navHostController = navController)
        }
    }}
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


fun shouldShowFab(currentDestination: String?): Boolean {
    val fabVisibleDestinations = listOf("home_screen") // Add paths where FAB should be visible
    return currentDestination in fabVisibleDestinations
}
