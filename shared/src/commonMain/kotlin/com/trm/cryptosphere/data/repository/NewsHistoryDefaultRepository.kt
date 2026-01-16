package com.trm.cryptosphere.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.trm.cryptosphere.core.base.nowDateTime
import com.trm.cryptosphere.data.db.dao.NewsHistoryDao
import com.trm.cryptosphere.data.db.entity.NewsHistoryEntity
import com.trm.cryptosphere.data.db.mapper.toDomain
import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NewsHistoryDefaultRepository(private val dao: NewsHistoryDao) : NewsHistoryRepository {
  override suspend fun addNewsToHistory(news: NewsItem) {
    dao.insert(
      NewsHistoryEntity(
        title = news.title,
        source = news.source,
        url = news.link,
        imgUrl = news.imgUrl,
        visitedAt = nowDateTime(),
      )
    )
  }

  override suspend fun updateNewsInHistory(news: NewsHistoryItem) {
    dao.insert(
      NewsHistoryEntity(
        title = news.title,
        source = news.source,
        url = news.url,
        imgUrl = news.imgUrl,
        visitedAt = nowDateTime(),
      )
    )
  }

  override fun getHistory(query: String): Flow<PagingData<NewsHistoryItem>> =
    Pager(config = PagingConfig(pageSize = 20), pagingSourceFactory = { dao.selectAll(query) })
      .flow
      .map { pagingData -> pagingData.map(NewsHistoryEntity::toDomain) }

  override suspend fun deleteAll() {
    dao.deleteAll()
  }

  override suspend fun deleteNewsHistory(id: Long) {
    dao.deleteById(id)
  }
}
