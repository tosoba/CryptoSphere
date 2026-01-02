package com.trm.cryptosphere.domain.repository

import androidx.paging.PagingData
import com.trm.cryptosphere.domain.model.TokenHistoryItem
import kotlinx.coroutines.flow.Flow

interface TokenHistoryRepository {
  suspend fun addTokenToHistory(tokenId: Int)

  fun getHistory(query: String): Flow<PagingData<TokenHistoryItem>>

  suspend fun deleteAllHistory()
}
