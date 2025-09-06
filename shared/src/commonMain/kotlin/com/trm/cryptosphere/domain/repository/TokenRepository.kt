package com.trm.cryptosphere.domain.repository

interface TokenRepository {
  suspend fun getTokensCount(): Int

  suspend fun performFullTokensSync()
}
