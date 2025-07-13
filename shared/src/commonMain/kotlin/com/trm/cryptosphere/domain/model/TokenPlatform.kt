package com.trm.cryptosphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenPlatform(
  val id: Int,
  val name: String,
  val symbol: String,
  val slug: String,
  val tokenAddress: String?,
)
