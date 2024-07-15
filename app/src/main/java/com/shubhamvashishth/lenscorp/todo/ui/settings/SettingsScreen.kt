package com.shubhamvashishth.lenscorp.todo.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

@Composable
fun SettingsScreen(navController: NavController, viewModel: SettingsScreenViewModelViewModel = hiltViewModel()) {
    val biometricEnabled by viewModel.biometricEnabled.collectAsState()
    val pushNotificationsEnabled by viewModel.pushNotificationsEnabled.collectAsState()
    val locationRemindersEnabled by viewModel.locationRemindersEnabled.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val notificationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.setPushNotificationsEnabled(true)
            } else {
                viewModel.setPushNotificationsEnabled(false)
            }
        }
    )

    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.setLocationRemindersEnabled(true)
            } else {
                viewModel.setLocationRemindersEnabled(false)
            }
        }
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Settings", style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enable Biometric")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = biometricEnabled,
                onCheckedChange = { enabled ->
                    if (enabled) {
                        val biometricManager = BiometricManager.from(context)
                        if (biometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS) {
                            val executor: Executor = ContextCompat.getMainExecutor(context)
                            val biometricPrompt = BiometricPrompt(context as FragmentActivity, executor,
                                object : BiometricPrompt.AuthenticationCallback() {
                                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                                        super.onAuthenticationSucceeded(result)
                                        viewModel.setBiometricEnabled(true)
                                    }

                                    override fun onAuthenticationFailed() {
                                        super.onAuthenticationFailed()
                                        viewModel.setBiometricEnabled(false)
                                    }
                                })

                            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                                .setTitle("Biometric Authentication")
                                .setSubtitle("Authenticate to enable biometrics")
                                .setNegativeButtonText("Cancel")
                                .build()

                            biometricPrompt.authenticate(promptInfo)
                        }
                    } else {
                        viewModel.setBiometricEnabled(false)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enable Push Notifications")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = pushNotificationsEnabled,
                onCheckedChange = { enabled ->
                    if (enabled) {
                        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                    } else {
                        viewModel.setPushNotificationsEnabled(false)
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Enable Location Reminders")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = locationRemindersEnabled,
                onCheckedChange = { enabled ->
                    if (enabled) {
                        locationPermissionLauncher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    } else {
                        viewModel.setLocationRemindersEnabled(false)
                    }
                }
            )
        }
    }
}


@Preview
@Composable
fun SettingsScreenPreview(){
    var navController= rememberNavController()
    SettingsScreen(navController = navController)
}