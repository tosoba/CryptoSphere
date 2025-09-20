package com.trm.cryptosphere.domain.repository

import com.trm.cryptosphere.domain.model.TokenItem

interface TokenRepository {
  suspend fun getTokensCount(): Int

  suspend fun performFullTokensSync()

  suspend fun getTokensMatchingSearchTerms(searchTerms: List<String>): List<TokenItem>
}
