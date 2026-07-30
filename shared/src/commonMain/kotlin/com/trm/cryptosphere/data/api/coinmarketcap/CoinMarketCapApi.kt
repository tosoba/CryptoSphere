package com.trm.cryptosphere.data.api.coinmarketcap

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildHttpClient
import com.trm.cryptosphere.core.network.resultOf
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokensInfoResponse
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokensResponse
import com.trm.cryptosphere.shared.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

class CoinMarketCapApi(private val client: HttpClient = buildHttpClient()) {
  suspend fun getTokens(limit: Int): NetworkResult<CmcTokensResponse> = resultOf {
    client.get("${BASE_URL}v3/cryptocurrency/listings/latest") {
      header("X-CMC_PRO_API_KEY", BuildKonfig.CMC_API_KEY)
      parameter("limit", limit)
    }
  }

  suspend fun getTokensInfo(
    id: String,
    skipInvalid: Boolean = true,
  ): NetworkResult<CmcTokensInfoResponse> = resultOf {
    client.get("${BASE_URL}v2/cryptocurrency/info") {
      header("X-CMC_PRO_API_KEY", BuildKonfig.CMC_API_KEY)
      parameter("id", id)
      parameter("skip_invalid", skipInvalid)
    }
  }

  companion object {
    private const val BASE_URL = "https://pro-api.coinmarketcap.com/"

    const val MAX_LIMIT = 5_000
  }
}
