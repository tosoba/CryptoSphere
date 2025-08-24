package com.trm.cryptosphere.domain.repository

import com.trm.cryptosphere.domain.model.NewsItem

interface NewsRepository {
  suspend fun getNews(): List<NewsItem>
}
