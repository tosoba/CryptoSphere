package com.trm.cryptosphere.domain.repository

import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsHistoryRepository {
  suspend fun addNewsToHistory(news: NewsItem)

  suspend fun updateNewsInHistory(news: NewsHistoryItem)

  fun getHistory(query: String, config: PagingConfig): Flow<PagingData<NewsHistoryItem>>

  suspend fun deleteAll()

  suspend fun deleteNewsHistory(id: Long)
}
