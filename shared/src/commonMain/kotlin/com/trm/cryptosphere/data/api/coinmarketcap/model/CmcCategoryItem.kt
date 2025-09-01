package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CmcCategoryItem(
  val id: String,
  val name: String,
  val title: String,
  val description: String,
  @SerialName("num_tokens") val numTokens: Int?,
  @SerialName("avg_price_change") val avgPriceChange: Double?,
  @SerialName("market_cap") val marketCap: Double?,
  @SerialName("market_cap_change") val marketCapChange: Double?,
  val volume: Double?,
  @SerialName("volume_change") val volumeChange: Double?,
  @SerialName("last_updated") val lastUpdated: String?,
)
