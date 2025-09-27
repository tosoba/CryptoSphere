package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsItem
import com.trm.cryptosphere.data.api.coinstats.toNewsItem
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsRepository

class NewsDefaultRepository(private val coinStatsApi: CoinStatsApi) : NewsRepository {
  override suspend fun getNewsPage(page: Int, limit: Int): List<NewsItem> =
    coinStatsApi
      .getNews(page = page + CoinStatsApi.PAGE_OFFSET, limit = limit)
      .getDataOrThrow()
      .result
      .map(CoinStatsNewsItem::toNewsItem)
}
