package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.Serializable

@Serializable
data class CmcTokensResponse(val status: CmcResponseStatus, val data: List<CmcTokenItem>)
