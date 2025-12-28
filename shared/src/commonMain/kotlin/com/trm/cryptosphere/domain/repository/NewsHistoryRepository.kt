package com.trm.cryptosphere.domain.repository

import com.trm.cryptosphere.domain.model.NewsItem

interface NewsHistoryRepository {
  suspend fun addNewsToHistory(news: NewsItem)
}
