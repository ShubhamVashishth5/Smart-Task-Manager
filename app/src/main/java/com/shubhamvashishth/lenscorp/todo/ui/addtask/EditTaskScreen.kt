package com.shubhamvashishth.lenscorp.todo.ui.addtask

import android.Manifest
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.adevinta.leku.LATITUDE
import com.adevinta.leku.LOCATION_ADDRESS
import com.adevinta.leku.LONGITUDE
import com.adevinta.leku.LocationPickerActivity
import com.adevinta.leku.locale.SearchZoneRect
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.shubhamvashishth.lenscorp.todo.GeofenceBroadcastReceiver
import com.shubhamvashishth.lenscorp.todo.ui.common.TodoTaskForm

@Composable
fun EditTaskScreen(navController: NavController, id: Int){

    var editAddTaskViewModel : EditAddTaskViewModel = hiltViewModel();


    val task = editAddTaskViewModel.todoTask.observeAsState()
    if (task.value==null) {  editAddTaskViewModel.loadTaskById(id)}


    var location by remember {
        mutableStateOf(task.value?.location?:"")
    }

    var latLng by remember {
        mutableStateOf(LatLng(0.0,0.0))
    }

    var currentLocation = task.value?.latLng

    val context = LocalContext.current
    val fusedLocationClient = remember { LocationServices.getFusedLocationProviderClient(context) }

    val geofencingClient = LocationServices.getGeofencingClient(context)

    val geofence = Geofence.Builder()
        .setRequestId("geofence_id2")
        .setCircularRegion(task.value?.latLng?.latitude?:0.0, task.value?.latLng?.latitude?:0.0, 1f)
        .setExpirationDuration(Geofence.NEVER_EXPIRE)
        .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT)
        .build()

    val geofencingRequest = GeofencingRequest.Builder()
        .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_DWELL)
        .addGeofence(geofence)
        .build()

    val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(context, GeofenceBroadcastReceiver::class.java)
        PendingIntent.getBroadcast(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }


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

        } else {
            Log.d("RESULT****", "CANCELLED")
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
                .withZipCodeHidden()
                .withSatelliteViewHidden()
                .withGooglePlacesEnabled()
                .withGoogleTimeZoneEnabled()
                .withVoiceSearchHidden()
                .withUnnamedRoadHidden()
                .build()
        }

        var locationNew= if(location=="") task.value?.location else location

        TodoTaskForm(task.value,  onSave = { task->
            Log.d("ok",task.taskId.toString())
            editAddTaskViewModel.deleteTodoTask(id)
            editAddTaskViewModel.saveTo(task)
            navController.popBackStack()
            scheduleNotification(context, task, getTimeRemainingInMillis(task.dueDate) )


            geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener {
                    Log.d("ok geo", "Geofence added successfully")
                }
                .addOnFailureListener { e ->
                    Log.e("Geofence", "Failed to add geofence", e)
                    // Additional debugging information
                    if (e is ApiException) {
                        when (e.statusCode) {
                            GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE -> Log.e("Geofence", "Geofence service not available")
                            GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES -> Log.e("Geofence", "Too many geofences")
                            GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS -> Log.e("Geofence", "Too many pending intents")
                            else -> Log.e("Geofence", "Unknown geofence error")
                        }
                    }}

        }, onCancel = {
            navController.popBackStack()
        }, onLocationClicked = { lekuActivityResultLauncher.launch(locationPickerIntent) }, location = locationNew?:"", latLng = latLng )

    }

//    TodoTaskForm(task.value, onSave = { task->
//        Log.d("ok",task.taskId.toString())
//        editAddTaskViewModel.deleteTodoTask(id)
//        editAddTaskViewModel.saveTo(task)
//     //   editAddTaskViewModel.edit(task)
//        navController.popBackStack()
//
//    }, onCancel = {
//        navController.popBackStack()
//    }, onLocationClicked = {})

}

