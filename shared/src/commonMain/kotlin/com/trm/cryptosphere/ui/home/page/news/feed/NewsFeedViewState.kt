package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.PagingData
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import androidx.paging.cachedIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsFeedViewState(getNewsUseCase: GetNewsUseCase, dispatchers: AppCoroutineDispatchers) :
  InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val pagingDataPresenter =
    object : PagingDataPresenter<NewsItem>() {
      override suspend fun presentPagingDataEvent(event: PagingDataEvent<NewsItem>) {
        _newsItemsSnapshotList.value = NewsItemsSnapshotList(snapshot())
      }
    }

  private val _newsItemsSnapshotList: MutableStateFlow<NewsItemsSnapshotList> =
    MutableStateFlow(NewsItemsSnapshotList(pagingDataPresenter.snapshot()))
  @Suppress("unused") // Used in Swift
  val newsItemsSnapshotList: StateFlow<NewsItemsSnapshotList> = _newsItemsSnapshotList.asStateFlow()

  @Suppress("unused") // Used in Swift
  val newsItemsLoadState = pagingDataPresenter.loadStateFlow

  val newsItems: Flow<PagingData<NewsItem>> = getNewsUseCase().cachedIn(scope)

  init {
    scope.launch { newsItems.collectLatest(pagingDataPresenter::collectFrom) }
  }

  override fun onDestroy() {
    scope.cancel()
  }

  @Suppress("unused") // Used in Swift
  fun loadMore() {
    pagingDataPresenter[pagingDataPresenter.size - 1]
  }

  @Suppress("unused") // Used in Swift
  fun retry() {
    pagingDataPresenter.retry()
  }
}
