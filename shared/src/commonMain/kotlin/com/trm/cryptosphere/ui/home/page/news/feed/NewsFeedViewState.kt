package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

internal class NewsFeedViewState(
  getNewsUseCase: GetNewsUseCase,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  val value: Flow<PagingData<NewsItem>> = getNewsUseCase().cachedIn(scope)

  override fun onDestroy() {
    scope.cancel()
  }
}
