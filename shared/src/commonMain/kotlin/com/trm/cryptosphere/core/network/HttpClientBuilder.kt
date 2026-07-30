package com.trm.cryptosphere.core.network

import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.HttpCache
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun buildHttpClient(
  loggingEnabled: Boolean = true,
  cacheStorage: CacheStorage? = null,
): HttpClient = HttpClient {
  install(ContentNegotiation) {
    json(
      Json {
        isLenient = true
        ignoreUnknownKeys = true
        explicitNulls = false
      }
    )
  }
  if (loggingEnabled) {
    install(Logging) {
      logger =
        object : Logger {
          override fun log(message: String) = co.touchlab.kermit.Logger.i(message)
        }
      level = LogLevel.ALL
    }
  }
  cacheStorage?.let {
    install(HttpCache) {
      publicStorage(it)
    }
  }
}
