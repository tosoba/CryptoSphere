package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.db.dao.NewsHistoryDao
import com.trm.cryptosphere.data.db.entity.NewsHistoryEntity
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class NewsHistoryDefaultRepository(private val dao: NewsHistoryDao) : NewsHistoryRepository {
  override suspend fun addNewsToHistory(news: NewsItem) {
    dao.insert(
      NewsHistoryEntity(
        url = news.link,
        imgUrl = news.imgUrl,
        visitedAt = Clock.System.now().toLocalDateTime(TimeZone.UTC),
      )
    )
  }
}
