package com.shubhamvashishth.lenscorp.todo.ui.homescreen

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shubhamvashishth.lenscorp.todo.ui.common.TaskCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomScreenViewModel = hiltViewModel()
    viewModel.loadTodoList()

    val taskList by viewModel.taskList.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()


    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            Header(
                onSearchQueryChange = { query -> viewModel.filterList(query) },
                onFilterOptionSelected = { filterOption -> viewModel.priorityAndStatusFilter(filterOption) },


            )
        }
    ) {

        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive((LocalConfiguration.current.screenWidthDp/2.2).dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                content = {
                    taskList.forEach { task ->

                        Log.d("ok time", task.dueDate.toString())
                        item(key = task.hashCode()) {
                            var isVisible by remember { mutableStateOf(true) }

                            AnimatedVisibility(
                                visible = isVisible,
                                enter = slideInVertically(
                                    initialOffsetY = { fullHeight -> fullHeight / 2 },
                                    animationSpec = tween(durationMillis = 300)
                                ),
                                exit = slideOutVertically(
                                    targetOffsetY = { fullHeight -> fullHeight },
                                    animationSpec = tween(durationMillis = 300)
                                )
                            ) {
                                TaskCard(
                                    title = task.title,
                                    dateTime = formatDate(task.dueDate),
                                    priority = task.taskPriority,
                                    description = task.description,
                                    location = task.location,
                                    onCheckboxChange = {
                                        coroutineScope.launch {
                                         //   isVisible = false
                                            delay(300) // Match the duration of the exit animation
                                            viewModel.deleteTodoTask(task) // Remove the task after animation completes
                                        }
                                    },
                                    onCardClick = {
                                        navController.navigate("edit/${task.taskId}")
                                        // Handle card click
                                    },
                                    isDefaultChecked = task.isCompleted
                                )
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxSize()
            )

        }


    }





}

@Composable
fun Header(
    onSearchQueryChange: (String) -> Unit,
    onFilterOptionSelected: (FilterOption) -> Unit,
) {
    var showDropdown by remember { mutableStateOf(false) }
    var showSubMenu by remember { mutableStateOf<FilterMenu?>(null) }
    var searchQuery by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { showDropdown = !showDropdown }) {
                Icon(Icons.Filled.MoreVert, contentDescription = "More options")
            }
            Box(
                modifier = Modifier
                    .height(40.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .border(1.dp, Color.Gray, shape = RoundedCornerShape(8.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .fillMaxWidth(), contentAlignment = Alignment.Center
                     // Clip the Box with rounded corners


            ) {
                BasicTextField(
                    value = searchQuery,
                    onValueChange = {
                        searchQuery = it
                        onSearchQueryChange(it)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }


        }

        // Show nested dropdown if needed
        if (showDropdown) {
            DropdownMenu(
                expanded = showDropdown,
                onDismissRequest = { showDropdown = false }
            ) {
                DropdownMenuItem(onClick = {
                    showDropdown = false
                    showSubMenu = FilterMenu.Priority
                }) {
                    Text("Priority")
                }
                DropdownMenuItem(onClick = {
                    showDropdown = false
                    showSubMenu = FilterMenu.Status
                }) {
                    Text("Status")
                }
            }
        }

        // Priority sub-menu
        if (showSubMenu == FilterMenu.Priority) {
            DropdownMenu(
                expanded = showSubMenu == FilterMenu.Priority,
                onDismissRequest = { showSubMenu = null }
            ) {
                PriorityOption.values().forEach { priorityOption ->
                    DropdownMenuItem(onClick = {
                        showSubMenu = null
                        onFilterOptionSelected(FilterOption.Priority(priorityOption))
                    }) {
                        Text(priorityOption.label)
                    }
                }
            }
        }

        // Status sub-menu
        if (showSubMenu == FilterMenu.Status) {
            DropdownMenu(
                expanded = showSubMenu == FilterMenu.Status,
                onDismissRequest = { showSubMenu = null }
            ) {
                StatusOption.values().forEach { statusOption ->
                    DropdownMenuItem(onClick = {
                        showSubMenu = null
                        onFilterOptionSelected(FilterOption.Status(statusOption))
                    }) {
                        Text(statusOption.label)
                    }
                }
            }
        }
    }

    LaunchedEffect(searchQuery) {
        Log.d("OKsss", searchQuery)
    }
}

enum class FilterMenu {
    Priority, Status
}

sealed class FilterOption {
    data class Priority(val option: PriorityOption) : FilterOption()
    data class Status(val option: StatusOption) : FilterOption()
}

enum class PriorityOption(val label: String) {
    Low("Low"),
    Medium("Medium"),
    High("High"),
    All("All")
}

enum class StatusOption(val label: String) {
    Complete("Complete"),
    Incomplete("Incomplete"),
    All("All")
}

@Preview
@Composable
fun PreviewHeader() {

        Header(
            onSearchQueryChange = { query -> /* Handle search query */ },
            onFilterOptionSelected = { filterOption -> /* Handle filter option */ }

        )

}

fun formatDate(date: Date): String {
    val outputFormat = SimpleDateFormat("EEEE, MMM dd yyyy, h:mm a", Locale.ENGLISH)
    return outputFormat.format(date)
}
// Preview function


