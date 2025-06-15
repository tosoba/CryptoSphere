package com.trm.cryptosphere.data.api.coinmarketcap

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildKtorfit
import com.trm.cryptosphere.data.api.coinmarketcap.model.TokensResponse
import de.jensklingenberg.ktorfit.http.GET
import de.jensklingenberg.ktorfit.http.Headers
import de.jensklingenberg.ktorfit.http.Query

interface CoinMarketCapApi {
  @GET("v1/cryptocurrency/listings/latest")
  @Headers("X-CMC_PRO_API_KEY: $COIN_MARKET_CAP_API_KEY")
  suspend fun getCryptocurrencies(@Query("limit") limit: Int): NetworkResult<TokensResponse>

  companion object {
    private const val BASE_URL = "https://pro-api.coinmarketcap.com/"

    operator fun invoke(): CoinMarketCapApi = buildKtorfit(BASE_URL).createCoinMarketCapApi()
  }
}
