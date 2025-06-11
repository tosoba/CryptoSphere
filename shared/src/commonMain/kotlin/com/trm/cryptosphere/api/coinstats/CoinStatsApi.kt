package com.trm.cryptosphere.api.coinstats

import com.trm.cryptosphere.api.coinstats.model.NewsResponse
import com.trm.cryptosphere.core.network.buildKtorfit
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface CoinStatsApi {
  @GET("news")
  @Headers("accept: application/json", "X-API-KEY: $COIN_STATS_API_KEY")
  suspend fun getNews(@Query page: Int = 1, @Query limit: Int = 100): Result<NewsResponse>

  companion object {
    private const val BASE_URL = "https://openapiv1.coinstats.app/"

    operator fun invoke(): CoinStatsApi = buildKtorfit(BASE_URL).createCoinStatsApi()
  }
}
