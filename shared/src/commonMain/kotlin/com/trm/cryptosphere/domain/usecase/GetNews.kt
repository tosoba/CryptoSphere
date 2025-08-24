package com.trm.cryptosphere.domain.usecase

import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetNews(
  private val tokenRepository: TokenRepository,
  private val newsRepository: NewsRepository,
) {
  suspend operator fun invoke(): List<NewsItem> = coroutineScope {
    val tokens = async { tokenRepository.getTokens() }
    val news = async { newsRepository.getNews() }
    tokens.await()
    news.await()
  }
}
