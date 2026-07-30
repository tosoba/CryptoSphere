package com.trm.cryptosphere.data.api.coinmarketcap

import com.trm.cryptosphere.core.network.NetworkResult
import com.trm.cryptosphere.core.network.buildHttpClient
import com.trm.cryptosphere.core.network.safeApiCall
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokensInfoResponse
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokensResponse
import com.trm.cryptosphere.shared.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.parameter

interface CoinMarketCapApi {
  suspend fun getTokens(limit: Int): NetworkResult<CmcTokensResponse>

  suspend fun getTokensInfo(
    id: String,
    skipInvalid: Boolean = true,
  ): NetworkResult<CmcTokensInfoResponse>

  companion object {
    private const val BASE_URL = "https://pro-api.coinmarketcap.com/"
    const val MAX_LIMIT = 5_000

    operator fun invoke(): CoinMarketCapApi =
      CoinMarketCapApiImpl(client = buildHttpClient(), baseUrl = BASE_URL)
  }
}

private class CoinMarketCapApiImpl(
  private val client: HttpClient,
  private val baseUrl: String,
) : CoinMarketCapApi {
  override suspend fun getTokens(limit: Int): NetworkResult<CmcTokensResponse> = safeApiCall {
    client.get("${baseUrl}v3/cryptocurrency/listings/latest") {
      header("X-CMC_PRO_API_KEY", BuildKonfig.CMC_API_KEY)
      parameter("limit", limit)
    }
  }

  override suspend fun getTokensInfo(
    id: String,
    skipInvalid: Boolean,
  ): NetworkResult<CmcTokensInfoResponse> = safeApiCall {
    client.get("${baseUrl}v2/cryptocurrency/info") {
      header("X-CMC_PRO_API_KEY", BuildKonfig.CMC_API_KEY)
      parameter("id", id)
      parameter("skip_invalid", skipInvalid)
    }
  }
}
