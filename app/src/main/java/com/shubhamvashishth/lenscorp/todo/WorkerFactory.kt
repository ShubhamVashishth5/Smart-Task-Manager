package com.shubhamvashishth.lenscorp.todo

import NotificationWorker
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.shubhamvashishth.lenscorp.todo.data.repository.TaskRepository
import javax.inject.Inject
import javax.inject.Provider

//
//class NotificationWorkerFactory @Inject constructor(
//    private val taskRepositoryProvider: Provider<TaskRepository>
//) : WorkerFactory() {
//
//    override fun createWorker(
//        appContext: Context,
//        workerClassName: String,
//        workerParameters: WorkerParameters
//    ): Worker? {
//        return when (workerClassName) {
//            NotificationWorker::class.java.name -> NotificationWorker(
//                appContext,
//                workerParameters,
//                taskRepositoryProvider.get()
//            )
//            else -> null
//        }
//    }
//}
