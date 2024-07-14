package com.shubhamvashishth.lenscorp.todo.ui.homescreen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.shubhamvashishth.lenscorp.todo.ui.common.TaskCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavController) {
    val viewModel: HomScreenViewModel = hiltViewModel()
    viewModel.loadTodoList()

    val taskList by viewModel.taskList.observeAsState(emptyList())
    val coroutineScope = rememberCoroutineScope()

    // State to manage FAB visibility
//    var fabVisible by remember { mutableStateOf(true) }
//    val listState = rememberLazyStaggeredGridState()
//
//    // Detect scrolling
//    LaunchedEffect(listState) {
//        var previousScrollOffset = listState.firstVisibleItemIndex
//
//        snapshotFlow { listState.firstVisibleItemIndex }
//            .collect { currentScrollOffset ->
//                val isScrollingUp = currentScrollOffset < previousScrollOffset
//                if (isScrollingUp) {
//                    fabVisible = true
//                } else {
//                    fabVisible = false
//                }
//                previousScrollOffset = currentScrollOffset
//            }
//    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(200.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                taskList.forEach { task ->
                    item(key = task.taskId) {
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
                                dateTime = task.dueDate.toString(),
                                priority = task.taskPriority,
                                description = task.description,
                                location = task.location,
                                onCheckboxChange = {
                                    coroutineScope.launch {
                                        isVisible = false
                                        delay(300) // Match the duration of the exit animation
                                        viewModel.deleteTodoTask(task.taskId) // Remove the task after animation completes
                                    }
                                },
                                onCardClick = {
                                    navController.navigate("edit/${task.taskId}")
                                    // Handle card click
                                }
                            )
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxSize()
        )

    }
}

