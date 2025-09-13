package com.trm.cryptosphere.domain.repository

import com.trm.cryptosphere.domain.model.NewsItem

interface NewsRepository {
  suspend fun getNewsPage(page: Int): List<NewsItem>
}
