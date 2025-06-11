package com.trm.cryptosphere.core.network

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.isSuccess

class NetworkResultConverterFactory : Converter.Factory {
  override fun suspendResponseConverter(
    typeData: TypeData,
    ktorfit: Ktorfit,
  ): Converter.SuspendResponseConverter<HttpResponse, NetworkResult<*>>? {
    if (typeData.typeInfo.type == NetworkResult::class) {
      return object : Converter.SuspendResponseConverter<HttpResponse, NetworkResult<*>> {
        override suspend fun convert(result: KtorfitResult): NetworkResult<*> {
          return when (result) {
            is KtorfitResult.Failure -> {
              NetworkResult.Exception(result.throwable)
            }
            is KtorfitResult.Success -> {
              if (result.response.status.isSuccess())
                NetworkResult.Success(result.response.body(typeData.typeArgs.first().typeInfo))
              else {
                NetworkResult.HttpError(result.response)
              }
            }
          }
        }
      }
    }
    return null
  }
}
