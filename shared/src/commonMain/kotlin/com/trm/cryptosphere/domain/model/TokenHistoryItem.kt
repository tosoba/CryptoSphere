package com.trm.cryptosphere.domain.model

import kotlinx.datetime.LocalDateTime

data class TokenHistoryItem(
  val id: Long,
  val tokenId: Int,
  val tokenName: String,
  val tokenSymbol: String,
  val visitedAt: LocalDateTime,
)
