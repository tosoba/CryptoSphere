package com.trm.cryptosphere.domain.repository

interface TokenHistoryRepository {
  suspend fun addTokenToHistory(tokenId: Int)
}
