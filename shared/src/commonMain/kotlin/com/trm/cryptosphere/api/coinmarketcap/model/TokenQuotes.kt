package com.trm.cryptosphere.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class TokenQuotes(@SerialName("USD") val usd: TokenQuote)
