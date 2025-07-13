package com.trm.cryptosphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenQuote(
  val price: Double,
  val volume24h: Double,
  val volumeChange24h: Double,
  val percentChange1h: Double,
  val percentChange24h: Double,
  val percentChange7d: Double,
  val percentChange30d: Double,
  val percentChange60d: Double,
  val percentChange90d: Double,
  val marketCap: Double,
  val marketCapDominance: Double,
  val fullyDilutedMarketCap: Double,
  val tvl: Double?,
  val lastUpdated: String,
)
