package com.trm.cryptosphere.domain.usecase

import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.CategoryRepository
import com.trm.cryptosphere.domain.repository.NewsRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class GetNews(
  private val tokenRepository: TokenRepository,
  private val categoryRepository: CategoryRepository,
  private val newsRepository: NewsRepository,
) {
  suspend operator fun invoke(): List<NewsItem> = coroutineScope {
    val tokensSync = launch {
      if (tokenRepository.getTokensCount() == 0) tokenRepository.performFullTokensSync()
    }
    val categories = async { categoryRepository.getCategories() }
    val news = async { newsRepository.getNews() }
    tokensSync.join()
    // TODO: enqueue WorkManager (and its iOS equivalent via expect/actual) for periodic token sync
    categories.await()
    news.await()
  }
}
