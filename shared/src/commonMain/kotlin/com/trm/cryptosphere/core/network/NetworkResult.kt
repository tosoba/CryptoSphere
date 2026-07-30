package com.trm.cryptosphere.core.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

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

suspend inline fun <reified T : Any> resultOf(block: () -> HttpResponse): NetworkResult<T> =
  try {
    val response = block()
    if (response.status.isSuccess()) {
      NetworkResult.Success(response.body<T>())
    } else {
      NetworkResult.HttpError(response)
    }
  } catch (e: Throwable) {
    NetworkResult.Exception(e)
  }
