package com.trm.cryptosphere.api.coinstats

import com.trm.cryptosphere.api.coinstats.converter.ResultConverterFactory
import com.trm.cryptosphere.api.coinstats.model.NewsResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query
import de.jensklingenberg.ktorfit.ktorfit
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

interface CoinStatsApi {
  @GET("news")
  @Headers("accept: application/json", "X-API-KEY: $COIN_STATS_API_KEY")
  suspend fun getNews(@Query page: Int = 1, @Query limit: Int = 100): Result<NewsResponse>

  companion object {
    private const val BASE_URL = "https://openapiv1.coinstats.app/"

    operator fun invoke(): CoinStatsApi =
      ktorfit {
          baseUrl(BASE_URL)
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
        .createCoinStatsApi()
  }
}
