package com.trm.cryptosphere.core.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import com.trm.cryptosphere.core.base.cancellableRunCatching
import com.trm.cryptosphere.domain.repository.TokenRepository
import java.util.concurrent.TimeUnit

class TokensSyncWorker(
  context: Context,
  workerParameters: WorkerParameters,
  private val tokenRepository: TokenRepository,
) : CoroutineWorker(context, workerParameters) {
  override suspend fun doWork(): Result =
    cancellableRunCatching {
        tokenRepository.performFullTokensSync()
        Result.success()
      }
      .getOrDefault(Result.failure())

  companion object {
    const val TAG = "TOKENS_SYNC"

    fun periodicRequest(): PeriodicWorkRequest =
      PeriodicWorkRequestBuilder<TokensSyncWorker>(
          repeatInterval = 1L,
          repeatIntervalTimeUnit = TimeUnit.DAYS,
        )
        .setConstraints(
          Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .setRequiresBatteryNotLow(true)
            .build()
        )
        .build()
  }
}
