package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.Serializable

@Serializable
data class TokensInfoResponse(
  val status: CoinMarketCapResponseStatus,
  val data: Map<String, TokenInfoResponseItem>,
)
