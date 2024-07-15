package com.shubhamvashishth.lenscorp.todo.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

            if (description != "" || location != "") {
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
                if (description != "") {
                    Text(
                        text = description,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                if (location != "") {
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
