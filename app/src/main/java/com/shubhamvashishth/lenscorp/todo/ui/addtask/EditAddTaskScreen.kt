package com.shubhamvashishth.lenscorp.todo.ui.addtask

import NotificationWorker
import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.adevinta.leku.ADDRESS
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.adevinta.leku.TIME_ZONE_DISPLAY_NAME
import com.adevinta.leku.TIME_ZONE_ID
import com.adevinta.leku.TRANSITION_BUNDLE
import com.adevinta.leku.ZIPCODE
import com.adevinta.leku.locale.SearchZoneRect
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.shubhamvashishth.lenscorp.todo.ui.common.TodoTaskForm
import java.util.Date
import java.util.concurrent.TimeUnit

@Composable
fun AddEditTaskScreen(navController: NavController){

    var editAddTaskViewModel : EditAddTaskViewModel = hiltViewModel();


    var location by remember {
        mutableStateOf("")
    }

    var latLng by remember {
        mutableStateOf(LatLng(0.0,0.0))
    }

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }
    var currentLocation by remember { mutableStateOf<LatLng?>(null) }

    val lekuActivityResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            Log.d("RESULT****", "OK")
            val latitude = data?.getDoubleExtra(LATITUDE, 0.0)
            Log.d("LATITUDE****", latitude.toString())
            val longitude = data?.getDoubleExtra(LONGITUDE, 0.0)
            Log.d("LONGITUDE****", longitude.toString())
            latLng = LatLng(latitude?:0.0, longitude?:0.0)
            val address = data?.getStringExtra(LOCATION_ADDRESS)
            location= address?:""
            Log.d("ADDRESS****", address.toString())
            val postalcode = data?.getStringExtra(ZIPCODE)
            Log.d("POSTALCODE****", postalcode.toString())
            val bundle = data?.getBundleExtra(TRANSITION_BUNDLE)
            Log.d("BUNDLE TEXT****", bundle?.getString("test").toString())
            val fullAddress = data?.getParcelableExtra<Address>(ADDRESS)
            if (fullAddress != null) {
                Log.d("FULL ADDRESS****", fullAddress.toString())
            }
            val timeZoneId = data?.getStringExtra(TIME_ZONE_ID)
            if (timeZoneId != null) {
                Log.d("TIME ZONE ID****", timeZoneId)
            }
            val timeZoneDisplayName = data?.getStringExtra(TIME_ZONE_DISPLAY_NAME)
            if (timeZoneDisplayName != null) {
                Log.d("TIME ZONE NAME****", timeZoneDisplayName)
            }
        } else {
            Log.d("RESULT****", "CANCELLED")
        }
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        } else {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                currentLocation = if (location != null) {
                    LatLng(location.latitude, location.longitude)
                } else {
                    LatLng(41.4036299, 2.1743558) // Default to a known location if unable to get current location
                }
            }
        }
    }

    if (currentLocation != null) {
        val locationPickerIntent = remember(currentLocation) {
            LocationPickerActivity.Builder(context)
                .withLocation(currentLocation!!.latitude, currentLocation!!.longitude)
                .withGeolocApiKey("AIzaSyCx36Z4o7Nl-ZSptkrbwmDqJdcS2T5coq8")
                .withGooglePlacesApiKey("AIzaSyCx36Z4o7Nl-ZSptkrbwmDqJdcS2T5coq8")
                .withSearchZone(SearchZoneRect(LatLng(currentLocation!!.latitude, currentLocation!!.longitude), LatLng(currentLocation!!.latitude, currentLocation!!.longitude)))
                .withDefaultLocaleSearchZone()
//                .shouldReturnOkOnBackPressed()
//                .withStreetHidden()
//                .withCityHidden()
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGooglePlacesEnabled()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build()
        }

        TodoTaskForm(onSave = { task->
            editAddTaskViewModel.saveTo(task)
            navController.popBackStack()
            scheduleNotification(context, task.taskId.toString(), getTimeRemainingInMillis(task.dueDate) )

        }, onCancel = {
            navController.popBackStack()
        }, onLocationClicked = { lekuActivityResultLauncher.launch(locationPickerIntent) }, location = location, latLng = latLng )

        Column {
            Button(onClick = { lekuActivityResultLauncher.launch(locationPickerIntent) }) {
                Text("Pick Location")
            }
            Text(text = "Current Location: ${currentLocation!!.latitude}, ${currentLocation!!.longitude}")
        }
    } else {
        Text(text = "Getting current location...")
    }

}


fun getTimeRemainingInMillis(targetDate: Date): Long {
    val currentTime = System.currentTimeMillis()
    val targetTime = targetDate.time
    val timeRemaining = targetTime - currentTime

    return maxOf(timeRemaining, 0) // Ensure we don't return a negative value if the target date is in the past
}


fun scheduleNotification(context: Context, taskId: String, triggerTime: Long) {
    Log.d("ok noti", triggerTime.toString())
    val workRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
        .setInitialDelay(0 , TimeUnit.MILLISECONDS)
        .setInputData(
            Data.Builder()
                .putString("TASK_ID", taskId)
                .build()
        )
        .build()

    WorkManager.getInstance(context).enqueue(workRequest)
}

