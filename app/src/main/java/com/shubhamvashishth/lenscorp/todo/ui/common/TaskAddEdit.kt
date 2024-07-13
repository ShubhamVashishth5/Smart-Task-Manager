package com.shubhamvashishth.lenscorp.todo.ui.common

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.shubhamvashishth.lenscorp.todo.data.model.Priority
import com.shubhamvashishth.lenscorp.todo.data.model.TodoTask
//import com.google.android.gms.maps.model.LatLng
import java.util.*


@Composable
fun TodoTaskForm(
  //  onLocationSelected: (LatLng) -> Unit,
    isEditable: Boolean = true,
    toggleVisibility: Boolean = true,
    onSave: (TodoTask) -> Unit,
    onCancel: () -> Unit,
) {
    var taskPriority by remember { mutableStateOf(Priority.LOW) }
    var title by remember { mutableStateOf(TextFieldValue("")) }
    var dueDate by remember { mutableStateOf<Date?>(null) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
  //  var location by remember { mutableStateOf<LatLng?>(null) }
    var editMode by remember { mutableStateOf(isEditable) }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    ) {
        if (toggleVisibility) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row {
                    IconButton(onClick = { editMode = !editMode }) {
                        Icon(
                            Icons.Filled.Edit,
                            contentDescription = if (editMode) "View Mode" else "Edit Mode",
                            tint = if (editMode) Color.Blue else Color.Gray
                        )
                    }
                    IconButton(onClick = { onCancel() }) {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = "Cancel",
                            tint = Color.Red
                        )
                    }
                    IconButton(onClick = {
                        var task = TodoTask(taskPriority,title.text,dueDate?:Date(), description.text, false, "Empty")
                        onSave.invoke(task)

                    }) {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = "Save",
                            tint = Color.Green
                        )
                    }
                }
            }
        }

        DropdownMenuPriority(priority = taskPriority, onPrioritySelected = { taskPriority = it }, enabled = editMode)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            enabled = editMode,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            label = { Text("Title") }
        )

        DateTimePickerField(date = dueDate, onDateSelected = { dueDate = it }, enabled = editMode)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            enabled = editMode,
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp),
            label = { Text("Description") }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Launch Leku Location Picker
                // onLocationSelected(selectedLocation) - handle location selected
            },
            enabled = editMode,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Pick Location")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                var task = TodoTask(taskPriority,title.text,dueDate?:Date(), description.text, false, "Empty")
                onSave.invoke(task)
                // onLocationSelected(selectedLocation) - handle location selected
            },
            enabled = editMode,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Save")
        }

//        location?.let {
//            Text("Selected Location: ${it.latitude}, ${it.longitude}", style = MaterialTheme.typography.body1)
//        }
    }
}

@Composable
fun DropdownMenuPriority(priority: Priority, onPrioritySelected: (Priority) -> Unit, enabled: Boolean) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = { expanded = true },
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Text(priority.name)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            Priority.values().forEach { priorityOption ->
                DropdownMenuItem(onClick = {
                    onPrioritySelected(priorityOption)
                    expanded = false
                }) {
                    Text(priorityOption.name)
                }
            }
        }
    }
}

@Composable
fun DateTimePickerField(date: Date?, onDateSelected: (Date) -> Unit, enabled: Boolean) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    date?.let { calendar.time = it }
    val dateFormat = java.text.DateFormat.getDateInstance(java.text.DateFormat.MEDIUM)
    val timeFormat = java.text.DateFormat.getTimeInstance(java.text.DateFormat.SHORT)

    val dateText = date?.let { dateFormat.format(it) } ?: "--"
    val timeText = date?.let { timeFormat.format(it) } ?: "--"

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            OutlinedButton(
                onClick = {
                    if (enabled) {
                        DatePickerDialog(
                            context,
                            { _, year, month, dayOfMonth ->
                                calendar.set(year, month, dayOfMonth)
                                onDateSelected(calendar.time)
                            },
                            calendar.get(Calendar.YEAR),
                            calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)
                        ).show()
                    }
                },
                enabled = enabled,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                Spacer(modifier = Modifier.width(8.dp))
                Text(dateText)
            }
        }
        Column {
            OutlinedButton(
                onClick = {
                    if (enabled) {
                        TimePickerDialog(
                            context,
                            { _, hourOfDay, minute ->
                                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                                calendar.set(Calendar.MINUTE, minute)
                                onDateSelected(calendar.time)
                            },
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            false
                        ).show()
                    }
                },
                enabled = enabled
            ) {
                Icon(Icons.Default.Home, contentDescription = "Select Time")
                Spacer(modifier = Modifier.width(8.dp))
                Text(timeText)
            }
        }
    }
}

