package com.shubhamvashishth.lenscorp.todo.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import com.shubhamvashishth.lenscorp.todo.data.model.Priority

@Composable
fun TaskCard(
    title: String,
    dateTime: String,
    priority: Priority,
    description: String,
    location: String,
    onCheckboxChange: (Boolean) -> Unit,
    onCardClick: () -> Unit,
    isDefaultChecked: Boolean
) {
    var isExpanded by remember { mutableStateOf(false) }
    var isChecked by remember { mutableStateOf(isDefaultChecked) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onCardClick() },
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = dateTime, fontSize = 14.sp, color = Color.Gray)
                }
                CircularCheckbox(
                    checked = isChecked,
                    onCheckedChange = {
                        isChecked = it
                        onCheckboxChange(it)
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                PriorityIndicator(priority = priority)
            }

            Spacer(modifier = Modifier.height(8.dp))

            if(description!=""||location!=""){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = if (isExpanded) "Show less" else "Show more",
                        color = MaterialTheme.colors.primary
                    )
                    Icon(
                        imageVector = if (isExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = null
                    )
                }
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                if(description!=""){
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                if(location!=""){
                    Text(
                        text = "Location: $location",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

@Composable
fun PriorityIndicator(priority: Priority) {
    val color = when (priority) {
        Priority.LOW -> Color.Green
        Priority.MEDIUM -> Color.Yellow
        Priority.HIGH -> Color.Red
    }

    Box(
        modifier = Modifier
            .size(12.dp)
            .background(color = color, shape = MaterialTheme.shapes.small)
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewTaskCard() {
    TaskCard(
        title = "Sample Taskdsa ksahdiuas shdiusa hdiusa  hsdaiudhsa uhsaids",
        dateTime = "2024-07-13 10:00 AM",
        priority = Priority.HIGH,
        description = "This is a detailed description of the task that is expanded when the card is clicked.",
        location = "123 Main St, Anytown",
        onCheckboxChange = {},
        onCardClick = {},
        true
    )
}

@Composable
fun CircularCheckbox(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val checkBoxColor = if (checked) MaterialTheme.colors.primary else Color.Gray
    val borderColor = if (checked) MaterialTheme.colors.primary else Color.Gray

    Box(
        modifier = modifier
            .size(24.dp)
            .clip(CircleShape)
            .background(if (checked) checkBoxColor else Color.Transparent)
            .border(2.dp, borderColor, CircleShape)
            .clickable { onCheckedChange(!checked) },
        contentAlignment = Alignment.Center
    ) {
        if (checked) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCircularCheckbox() {
    var isChecked by remember { mutableStateOf(true) }
    CircularCheckbox(
        checked = isChecked,
        onCheckedChange = { isChecked = it }
    )
}
