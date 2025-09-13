package com.trm.cryptosphere.core.base

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CancellationException

inline fun <T, R> T.cancellableRunCatching(block: T.() -> R): Result<R> =
  try {
    Result.success(block())
  } catch (cancellationException: CancellationException) {
    throw cancellationException
  } catch (throwable: Throwable) {
    Logger.e("cancellableRunCatching", throwable)
    Result.failure(throwable)
  }
