package com.trm.cryptosphere.domain.repository

interface HistoryRepository {
  suspend fun createNewHistory(tokenId: Int): Long

  suspend fun addItemToHistory(historyId: Long, previousItemTokenId: Int, newItemTokenId: Int)
}
