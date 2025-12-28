package com.trm.cryptosphere.domain.repository

interface NewsHistoryRepository {
  suspend fun addNewsToHistory(url: String)
}
