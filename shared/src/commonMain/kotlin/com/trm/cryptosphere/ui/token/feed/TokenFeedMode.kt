package com.trm.cryptosphere.ui.token.feed

import kotlinx.serialization.Serializable

@Serializable
sealed interface TokenFeedMode {
  val tokenId: Int

  data class HistoryFirst(override val tokenId: Int) : TokenFeedMode

  data class HistoryContinuation(
    val historyId: Int,
    val previousTokenId: Int,
    override val tokenId: Int,
  ) : TokenFeedMode
}
