package com.shubhamvashishth.lenscorp.todo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.android.gms.maps.model.LatLng
import java.util.Date

@Entity
data class TodoTask(
    @ColumnInfo val taskPriority: Priority,
    @ColumnInfo val title: String,
    @ColumnInfo val dueDate: Date,
    @ColumnInfo val description: String,
    @ColumnInfo var isCompleted: Boolean,
    @ColumnInfo val location: String,
    @ColumnInfo val latLng: LatLng,
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0
    )

enum class Priority{
    LOW, MEDIUM, HIGH
}

