package com.trm.cryptosphere.data.api.coinmarketcap

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildKtorfit
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcCategoriesResponse
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokensInfoResponse
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokensResponse
import com.trm.cryptosphere.shared.BuildKonfig
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface CoinMarketCapApi {
  @GET("v1/cryptocurrency/listings/latest")
  @Headers("X-CMC_PRO_API_KEY: ${BuildKonfig.CMC_API_KEY}")
  suspend fun getTokens(@Query("limit") limit: Int): NetworkResult<CmcTokensResponse>

  @GET("v2/cryptocurrency/info")
  @Headers("X-CMC_PRO_API_KEY: ${BuildKonfig.CMC_API_KEY}")
  suspend fun getTokensInfo(
    @Query("id", encoded = true) id: String,
    @Query("skip_invalid") skipInvalid: Boolean = true,
  ): NetworkResult<CmcTokensInfoResponse>

  @GET("v1/cryptocurrency/categories")
  @Headers("X-CMC_PRO_API_KEY: ${BuildKonfig.CMC_API_KEY}")
  suspend fun getCategories(@Query("limit") limit: Int): NetworkResult<CmcCategoriesResponse>

  companion object {
    private const val BASE_URL = "https://pro-api.coinmarketcap.com/"
    const val MAX_LIMIT = 5_000

    operator fun invoke(): CoinMarketCapApi = buildKtorfit(BASE_URL).createCoinMarketCapApi()
  }
}
