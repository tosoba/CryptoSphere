package com.trm.cryptosphere.core.worker

import android.content.Context
import androidx.startup.Initializer
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.trm.cryptosphere.CryptoSphereApplication
import com.trm.cryptosphere.core.work.TokensSyncWorker

class WorkManagerInitializer : Initializer<WorkManager> {
  override fun create(context: Context): WorkManager {
    WorkManager.initialize(
      context,
      Configuration.Builder()
        .setWorkerFactory(
          object : WorkerFactory() {
            override fun createWorker(
              appContext: Context,
              workerClassName: String,
              workerParameters: WorkerParameters,
            ): ListenableWorker? =
              when (workerClassName) {
                TokensSyncWorker::class.qualifiedName -> {
                  TokensSyncWorker(
                    context = appContext,
                    workerParameters = workerParameters,
                    tokenRepository =
                      (appContext as CryptoSphereApplication)
                        .dependencyContainer
                        .tokenRepository
                        .value,
                  )
                }
                else -> {
                  null
                }
              }
          }
        )
        .build(),
    )
    return WorkManager.getInstance(context)
  }

  override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}
