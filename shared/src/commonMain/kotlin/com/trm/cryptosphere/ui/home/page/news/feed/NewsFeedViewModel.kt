package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.PagingConfig
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.PagingItemsState
import com.trm.cryptosphere.data.api.coinstats.CoinStatsApi
import com.trm.cryptosphere.domain.model.NewsFeedItem
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

  val newsPagingState: PagingItemsState<NewsFeedItem> =
    PagingItemsState(scope) {
      getNewsFeedUseCase(
        PagingConfig(
          pageSize = CoinStatsApi.MAX_LIMIT,
          prefetchDistance = PREFETCH_DISTANCE,
          initialLoadSize = CoinStatsApi.MAX_LIMIT,
        )
      )
    }

  fun onLinkClick(news: NewsItem) {
    scope.launch { newsHistoryRepository.addNewsToHistory(news) }
  }

  override fun onDestroy() {
    scope.cancel()
  }

  companion object {
    const val PREFETCH_DISTANCE = 10
  }
}
