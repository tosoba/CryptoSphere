package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class CmcTokenQuotes(@SerialName("USD") val usd: CmcTokenQuote)
