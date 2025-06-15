package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenResponseItem(
  val id: Int,
  val name: String,
  val symbol: String,
  val slug: String,
  @SerialName("num_market_pairs") val numMarketPairs: Int,
  @SerialName("date_added") val dateAdded: String,
  val tags: List<String>,
  @SerialName("max_supply") val maxSupply: Double?,
  @SerialName("circulating_supply") val circulatingSupply: Double,
  @SerialName("total_supply") val totalSupply: Double,
  @SerialName("infinite_supply") val infiniteSupply: Boolean,
  val platform: TokenPlatform?,
  @SerialName("cmc_rank") val cmcRank: Int,
  @SerialName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Double?,
  @SerialName("self_reported_market_cap") val selfReportedMarketCap: Double?,
  @SerialName("tvl_ratio") val tvlRatio: Double?,
  @SerialName("last_updated") val lastUpdated: String,
  val quote: TokenQuotes,
)
