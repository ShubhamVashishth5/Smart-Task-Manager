package com.shubhamvashishth.lenscorp.todo.di

import android.content.Context
import androidx.room.Room
import com.shubhamvashishth.lenscorp.todo.data.dao.TaskDao
import com.shubhamvashishth.lenscorp.todo.data.database.TaskDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): TaskDatabase {
        return Room.databaseBuilder(
            appContext,
            TaskDatabase::class.java,
            "app_database"
        ).build()
    }

    @Provides
    fun provideTaskDao(database: TaskDatabase): TaskDao {
        return database.taskDao()
    }

}