package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.db.dao.HistoryDao
import com.trm.cryptosphere.domain.repository.HistoryRepository

class HistoryDefaultRepository(private val dao: HistoryDao) : HistoryRepository {
  override suspend fun createNewHistory(tokenId: Int): Long = dao.insertNewHistory(tokenId)

  override suspend fun addItemToHistory(
    historyId: Long,
    previousItemTokenId: Int,
    newItemTokenId: Int,
  ) {
    dao.addHistoryItem(
      historyId = historyId,
      previousItemTokenId = previousItemTokenId,
      newItemTokenId = newItemTokenId,
    )
  }
}
