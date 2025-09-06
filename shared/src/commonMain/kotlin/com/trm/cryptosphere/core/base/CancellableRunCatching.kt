package com.trm.cryptosphere.core.base

import co.touchlab.kermit.Logger
import kotlinx.coroutines.CancellationException

inline fun <T, R> T.cancellableRunCatching(block: T.() -> R): Result<R> =
  try {
    Result.success(block())
  } catch (ce: CancellationException) {
    throw ce
  } catch (e: Throwable) {
    Logger.e("cancellableRunCatching", e)
    Result.failure(e)
  }
