package com.trm.cryptosphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenItem(
  val id: Int,
  val name: String,
  val symbol: String,
  val slug: String,
  val numMarketPairs: Int,
  val dateAdded: String,
  val maxSupply: Double?,
  val circulatingSupply: Double,
  val totalSupply: Double,
  val infiniteSupply: Boolean,
  val cmcRank: Int,
  val selfReportedCirculatingSupply: Double?,
  val selfReportedMarketCap: Double?,
  val tvlRatio: Double?,
  val lastUpdated: String,
  val quote: TokenQuote,
  val tagNames: List<String> = emptyList(),
)

val TokenItem.logoUrl: String
  get() = "https://s2.coinmarketcap.com/static/img/coins/128x128/$id.png"

val TokenItem.shareUrl: String
  get() = "https://coinmarketcap.com/currencies/$slug"
