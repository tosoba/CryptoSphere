package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenQuote(
  val price: Double,
  @SerialName("volume_24h") val volume24h: Double,
  @SerialName("volume_change_24h") val volumeChange24h: Double,
  @SerialName("percent_change_1h") val percentChange1h: Double,
  @SerialName("percent_change_24h") val percentChange24h: Double,
  @SerialName("percent_change_7d") val percentChange7d: Double,
  @SerialName("percent_change_30d") val percentChange30d: Double,
  @SerialName("percent_change_60d") val percentChange60d: Double,
  @SerialName("percent_change_90d") val percentChange90d: Double,
  @SerialName("market_cap") val marketCap: Double,
  @SerialName("market_cap_dominance") val marketCapDominance: Double,
  @SerialName("fully_diluted_market_cap") val fullyDilutedMarketCap: Double,
  val tvl: Double?,
  @SerialName("last_updated") val lastUpdated: String,
)
