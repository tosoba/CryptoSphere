package com.trm.cryptosphere.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.trm.cryptosphere.data.db.dao.TokenHistoryDao
import com.trm.cryptosphere.data.db.entity.TokenHistoryEntity
import com.trm.cryptosphere.data.db.mapper.toDomain
import com.trm.cryptosphere.domain.model.TokenHistoryItem
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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

  override fun getHistory(): Flow<PagingData<TokenHistoryItem>> {
    return Pager(
        config = PagingConfig(pageSize = 20, enablePlaceholders = false),
        pagingSourceFactory = { dao.getAllPagingSource() },
      )
      .flow
      .map { pagingData -> pagingData.map { it.toDomain() } }
  }
}
