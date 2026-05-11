package com.trm.cryptosphere.manager

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import com.trm.cryptosphere.core.work.TokensSyncWorker
import com.trm.cryptosphere.domain.manager.BackgroundJobsManager

class AndroidBackgroundJobsManager(private val context: Context) : BackgroundJobsManager {
  override fun enqueuePeriodicTokensSync() {
    WorkManager.getInstance(context)
      .enqueueUniquePeriodicWork(
        TokensSyncWorker.TAG,
        ExistingPeriodicWorkPolicy.KEEP,
        TokensSyncWorker.periodicRequest(),
      )
  }
}
