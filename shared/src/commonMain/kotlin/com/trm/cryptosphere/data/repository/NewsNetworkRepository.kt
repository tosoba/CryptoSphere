package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.data.api.coinstats.model.CoinStatsNewsItem
import com.trm.cryptosphere.data.api.coinstats.toNewsItem
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsRepository

class NewsNetworkRepository(private val api: CoinStatsApi) : NewsRepository {
  override suspend fun getNews(): List<NewsItem> =
    api.getNews().getDataOrThrow().result.map(CoinStatsNewsItem::toNewsItem)
}
