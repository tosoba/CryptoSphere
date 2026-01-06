package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.PagingItemsState
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import com.trm.cryptosphere.domain.usecase.GetNewsFeedUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class NewsFeedViewModel(
  getNewsFeedUseCase: GetNewsFeedUseCase,
  private val newsHistoryRepository: NewsHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  val newsPagingState = PagingItemsState(scope, getNewsFeedUseCase::invoke)

  fun onLinkClick(news: NewsItem) {
    scope.launch { newsHistoryRepository.addNewsToHistory(news) }
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
