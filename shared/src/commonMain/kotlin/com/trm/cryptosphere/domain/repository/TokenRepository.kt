package com.trm.cryptosphere.domain.repository

import com.trm.cryptosphere.domain.model.TokenItem

interface TokenRepository {
  suspend fun getTokens(): List<TokenItem>
}
