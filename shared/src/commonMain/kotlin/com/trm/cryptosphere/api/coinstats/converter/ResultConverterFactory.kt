package com.trm.cryptosphere.api.coinstats.converter

import de.jensklingenberg.ktorfit.Ktorfit
import de.jensklingenberg.ktorfit.converter.Converter
import de.jensklingenberg.ktorfit.converter.KtorfitResult
import de.jensklingenberg.ktorfit.converter.TypeData
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

class ResultConverterFactory : Converter.Factory {
  override fun suspendResponseConverter(
    typeData: TypeData,
    ktorfit: Ktorfit,
  ): Converter.SuspendResponseConverter<HttpResponse, Result<*>>? {
    if (typeData.typeInfo.type == Result::class) {
      return object : Converter.SuspendResponseConverter<HttpResponse, Result<*>> {
        override suspend fun convert(result: KtorfitResult): Result<*> {
          return when (result) {
            is KtorfitResult.Failure -> {
              Result.failure<Any>(result.throwable)
            }
            is KtorfitResult.Success -> {
              Result.success<Any>(result.response.body(typeData.typeArgs.first().typeInfo))
            }
          }
        }
      }
    }
    return null
  }
}
