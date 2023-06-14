package com.tanvir.training.todoappbatch03.scheduletodo

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

class WorkmanagerUtils(val context: Context, val name: String, val delay: Long) {

    fun schedule() {
        /*val constraint = Constraints.Builder()
            .setRequiresCharging(true)
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .setRequiresStorageNotLow(true)
            .setRequiresDeviceIdle(false)
            .build()*/

        val request = OneTimeWorkRequestBuilder<NotificationWorker>()
            //.setConstraints(constraint)
            .setInputData(workDataOf("name" to name))
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .addTag(name)
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}