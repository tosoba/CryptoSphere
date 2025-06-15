package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenPlatform(
  val id: Int,
  val name: String,
  val symbol: String,
  val slug: String,
  @SerialName("token_address") val tokenAddress: String,
)
