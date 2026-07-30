package com.trm.cryptosphere.data.api.coinstats

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildHttpClient
import com.trm.cryptosphere.core.network.safeApiCall
import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsResponse
import com.trm.cryptosphere.shared.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

interface CoinStatsApi {
  suspend fun getNews(
    page: Int = PAGE_OFFSET,
    limit: Int = MAX_LIMIT,
  ): NetworkResult<CoinStatsNewsResponse>

  companion object {
    private const val BASE_URL = "https://openapiv1.coinstats.app/"
    const val PAGE_OFFSET = 1 // API uses 1-based indexing...
    const val MAX_LIMIT = 100
    const val MAX_PAGE = 100

    operator fun invoke(cacheStorage: CacheStorage?): CoinStatsApi =
      CoinStatsApiImpl(client = buildHttpClient(cacheStorage = cacheStorage), baseUrl = BASE_URL)
  }
}

private class CoinStatsApiImpl(
  private val client: HttpClient,
  private val baseUrl: String,
) : CoinStatsApi {
  override suspend fun getNews(page: Int, limit: Int): NetworkResult<CoinStatsNewsResponse> =
    safeApiCall {
      client.get("${baseUrl}news") {
        header("X-API-KEY", BuildKonfig.COIN_NEWS_API_KEY)
        parameter("page", page)
        parameter("limit", limit)
      }
    }
}
