package com.trm.cryptosphere.api.coinmarketcap.model

import kotlinx.serialization.Serializable

@Serializable
data class TokensResponse(
  val status: CoinMarketCapResponseStatus,
  val data: List<TokenResponseItem>,
)
