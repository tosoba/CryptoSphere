package com.trm.cryptosphere.ui.token.feed

import kotlinx.serialization.Serializable

@Serializable
sealed interface TokenFeedMode {
  val tokenId: Int

  @Serializable data class HistoryFirst(override val tokenId: Int) : TokenFeedMode

  @Serializable
  data class HistoryContinuation(
    val historyId: Long,
    val previousTokenId: Int,
    override val tokenId: Int,
  ) : TokenFeedMode
}
