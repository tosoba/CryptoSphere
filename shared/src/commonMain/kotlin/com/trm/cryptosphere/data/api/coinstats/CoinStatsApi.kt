package com.trm.cryptosphere.data.api.coinstats

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildKtorfit
import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsResponse
import com.trm.cryptosphere.shared.BuildKonfig
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query
import io.ktor.client.plugins.cache.storage.CacheStorage

interface CoinStatsApi {
  @GET("news")
  @Headers("X-API-KEY: ${BuildKonfig.COIN_NEWS_API_KEY}")
  suspend fun getNews(
    @Query page: Int = PAGE_OFFSET,
    @Query limit: Int = MAX_LIMIT,
  ): NetworkResult<CoinStatsNewsResponse>

  companion object {
    private const val BASE_URL = "https://openapiv1.coinstats.app/"
    const val PAGE_OFFSET = 1 // API uses 1-based indexing...
    const val MAX_LIMIT = 100
    const val MAX_PAGE = 100

    operator fun invoke(cacheStorage: CacheStorage?): CoinStatsApi =
      buildKtorfit(baseUrl = BASE_URL, cacheStorage = cacheStorage).createCoinStatsApi()
  }
}
