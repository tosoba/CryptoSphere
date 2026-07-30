package com.trm.cryptosphere.data.api.coinstats

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildHttpClient
import com.trm.cryptosphere.core.network.resultOf
import com.trm.cryptosphere.data.api.coinmarketcap.CoinMarketCapApi
import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsResponse
import com.trm.cryptosphere.shared.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.cache.storage.CacheStorage
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class CoinStatsApi(private val client: HttpClient) {
  constructor(
    cacheStorage: CacheStorage?
  ) : this(client = buildHttpClient(cacheStorage = cacheStorage))

  suspend fun getNews(
    page: Int = PAGE_OFFSET,
    limit: Int = CoinMarketCapApi.MAX_LIMIT,
  ): NetworkResult<CoinStatsNewsResponse> = resultOf {
    client.get("${BASE_URL}news") {
      header("X-API-KEY", BuildKonfig.COIN_NEWS_API_KEY)
      parameter("page", page)
      parameter("limit", limit)
    }
  }

  companion object {
    private const val BASE_URL = "https://openapiv1.coinstats.app/"

    const val PAGE_OFFSET = 1 // API uses 1-based indexing...
    const val MAX_LIMIT = 100
    const val MAX_PAGE = 100
  }
}
