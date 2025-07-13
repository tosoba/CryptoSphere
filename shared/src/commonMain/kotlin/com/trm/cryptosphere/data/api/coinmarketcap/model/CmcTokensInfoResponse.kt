package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.Serializable

@Serializable
data class CmcTokensInfoResponse(
  val status: CmcResponseStatus,
  val data: Map<String, CmcTokenInfoItem>,
)
