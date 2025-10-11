package com.trm.cryptosphere.core.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.trm.cryptosphere.core.base.cancellableRunCatching
import com.trm.cryptosphere.domain.repository.TokenRepository

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
}
