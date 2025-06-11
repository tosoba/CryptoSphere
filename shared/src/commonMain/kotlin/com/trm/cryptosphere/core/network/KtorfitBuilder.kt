package com.trm.cryptosphere.core.network

import com.trm.cryptosphere.api.coinstats.converter.ResultConverterFactory
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun buildKtorfit(baseUrl: String) = ktorfit {
  baseUrl(baseUrl)
  httpClient(
    HttpClient {
      install(ContentNegotiation) {
        json(
          Json {
            isLenient = true
            ignoreUnknownKeys = true
          }
        )
      }
    }
  )
  converterFactories(ResultConverterFactory())
}
