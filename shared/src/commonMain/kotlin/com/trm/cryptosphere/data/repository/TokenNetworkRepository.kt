package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.store.TokenStore
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository
import org.mobilenativefoundation.store.store5.impl.extensions.get

class TokenNetworkRepository(private val store: TokenStore) : TokenRepository {
  override suspend fun getTokens(): List<TokenItem> = store.get(0)
}
