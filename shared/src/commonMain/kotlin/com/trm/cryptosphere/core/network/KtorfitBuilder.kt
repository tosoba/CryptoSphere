package com.trm.cryptosphere.core.network

import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun buildKtorfit(baseUrl: String, loggingEnabled: Boolean = true) = ktorfit {
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
      if (loggingEnabled) {
        install(Logging) {
          logger =
            object : Logger {
              override fun log(message: String) = println("Ktorfit: $message")
            }
          level = LogLevel.ALL
        }
      }
    }
  )
  converterFactories(NetworkResultConverterFactory())
}
