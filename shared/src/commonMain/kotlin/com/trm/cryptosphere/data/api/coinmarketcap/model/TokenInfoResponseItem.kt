package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenInfoResponseItem(
  val id: Int,
  val name: String,
  val symbol: String,
  val category: String?,
  val description: String?,
  val slug: String?,
  val logo: String?,
  val subreddit: String?,
  val notice: String?,
  val tags: List<String>?,
  @SerialName("tag-names") val tagNames: List<String>?,
  @SerialName("tag-groups") val tagGroups: List<String>?,
  val urls: TokenUrls?,
  val platform: TokenPlatform?,
  @SerialName("date_added") val dateAdded: String?,
  @SerialName("twitter_username") val twitterUsername: String?,
  @SerialName("date_launched") val dateLaunched: String?,
  @SerialName("self_reported_circulating_supply") val selfReportedCirculatingSupply: Double?,
  @SerialName("self_reported_tags") val selfReportedTags: List<String>?,
  @SerialName("self_reported_market_cap") val selfReportedMarketCap: Double?,
  @SerialName("infinite_supply") val infiniteSupply: Boolean?,
)
