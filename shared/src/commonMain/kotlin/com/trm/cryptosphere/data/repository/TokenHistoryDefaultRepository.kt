package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.db.dao.TokenHistoryDao
import com.trm.cryptosphere.data.db.entity.TokenHistoryEntity
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class TokenHistoryDefaultRepository(private val dao: TokenHistoryDao) : TokenHistoryRepository {
  override suspend fun addTokenToHistory(tokenId: Int) {
    dao.insert(
      TokenHistoryEntity(
        tokenId = tokenId,
        visitedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
      )
    )
  }
}
