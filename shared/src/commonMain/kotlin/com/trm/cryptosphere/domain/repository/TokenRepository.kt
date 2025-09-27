package com.trm.cryptosphere.domain.repository

import androidx.paging.PagingData
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
  suspend fun getTokensCount(): Int

  suspend fun performFullTokensSync()

  suspend fun getTokensMatchingSearchTerms(searchTerms: List<String>): List<TokenItem>

  suspend fun getTokensBySharedTags(symbol: String): Flow<PagingData<TokenItem>>
}
