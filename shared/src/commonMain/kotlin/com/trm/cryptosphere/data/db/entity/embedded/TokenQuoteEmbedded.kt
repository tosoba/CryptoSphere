package com.trm.cryptosphere.data.db.entity.embedded

import androidx.room.ColumnInfo

data class TokenQuoteEmbedded(
  val price: Double,
  @ColumnInfo(name = "volume_24h") val volume24h: Double,
  @ColumnInfo(name = "volume_change_24h") val volumeChange24h: Double,
  @ColumnInfo(name = "percent_change_1h") val percentChange1h: Double,
  @ColumnInfo(name = "percent_change_24h") val percentChange24h: Double,
  @ColumnInfo(name = "percent_change_7d") val percentChange7d: Double,
  @ColumnInfo(name = "percent_change_30d") val percentChange30d: Double,
  @ColumnInfo(name = "percent_change_60d") val percentChange60d: Double,
  @ColumnInfo(name = "percent_change_90d") val percentChange90d: Double,
  @ColumnInfo(name = "market_cap") val marketCap: Double,
  @ColumnInfo(name = "market_cap_dominance") val marketCapDominance: Double,
  @ColumnInfo(name = "fully_diluted_market_cap") val fullyDilutedMarketCap: Double,
  val tvl: Double?,
  @ColumnInfo(name = "last_updated") val lastUpdated: String,
)
