package com.trm.cryptosphere.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.Flow

interface TokenRepository {
  suspend fun getTokensCount(): Int

  suspend fun performFullTokensSync()

  suspend fun getTokensMatchingSearchTerms(searchTerms: List<String>): List<TokenItem>

  fun getTokensBySharedTags(id: Int, config: PagingConfig): Flow<PagingData<TokenItem>>

  fun getTokens(query: String, config: PagingConfig): Flow<PagingData<TokenItem>>

  suspend fun getTokensByIds(ids: List<Int>): List<TokenItem>
}
