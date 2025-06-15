package com.trm.cryptosphere.data.api.coinstats

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildKtorfit
import com.trm.cryptosphere.data.api.coinstats.model.NewsResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface CoinStatsApi {
  @GET("news")
  @Headers("X-API-KEY: $COIN_STATS_API_KEY")
  suspend fun getNews(@Query page: Int = 1, @Query limit: Int = 100): NetworkResult<NewsResponse>

  companion object {
    private const val BASE_URL = "https://openapiv1.coinstats.app/"

    operator fun invoke(): CoinStatsApi = buildKtorfit(BASE_URL).createCoinStatsApi()
  }
}
