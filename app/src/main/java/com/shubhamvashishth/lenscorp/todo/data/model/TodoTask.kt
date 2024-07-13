package com.shubhamvashishth.lenscorp.todo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import java.util.Date

@Entity
data class TodoTask(
    @ColumnInfo val taskPriority: Priority,
    @ColumnInfo val title: String,
    @ColumnInfo val dueDate: Date,
    @ColumnInfo val description: String,
    @ColumnInfo val isCompleted: Boolean,
    @ColumnInfo val location: String,
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0
    )

enum class Priority{
    LOW, MEDIUM, HIGH
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}