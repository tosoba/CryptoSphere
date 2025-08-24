package com.trm.cryptosphere.core.network

import io.ktor.client.statement.HttpResponse

sealed interface NetworkResult<out T : Any> {
  data class Success<out T : Any>(val data: T) : NetworkResult<T>

  data class HttpError(val response: HttpResponse) : NetworkResult<Nothing>, Throwable()

  data class Exception(val throwable: Throwable) : NetworkResult<Nothing>

  fun getDataOrThrow(): T =
    when (this) {
      is Success -> data
      is HttpError -> throw this
      is Exception -> throw throwable
    }
}
