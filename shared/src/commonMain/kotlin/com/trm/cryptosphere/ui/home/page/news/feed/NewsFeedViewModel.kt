package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import androidx.paging.cachedIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.model.NewsFeedItem
import com.trm.cryptosphere.domain.usecase.GetNewsFeedUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewsFeedViewModel(
  getNewsFeedUseCase: GetNewsFeedUseCase,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val pagingDataPresenter =
    object : PagingDataPresenter<NewsFeedItem>() {
      override suspend fun presentPagingDataEvent(event: PagingDataEvent<NewsFeedItem>) {
        _newsFeedItemsSnapshotList.value = NewsFeedItemsSnapshotList(snapshot())
      }
    }

  private val _newsFeedItemsSnapshotList: MutableStateFlow<NewsFeedItemsSnapshotList> =
    MutableStateFlow(NewsFeedItemsSnapshotList(pagingDataPresenter.snapshot()))
  @Suppress("unused") // Used in Swift
  val newsFeedItemsSnapshotList: StateFlow<NewsFeedItemsSnapshotList> =
    _newsFeedItemsSnapshotList.asStateFlow()

  @Suppress("unused") // Used in Swift
  val newsItemsLoadStates: StateFlow<CombinedLoadStates?> = pagingDataPresenter.loadStateFlow

  val newsFeedItems: Flow<PagingData<NewsFeedItem>> = getNewsFeedUseCase().cachedIn(scope)

  init {
    scope.launch { newsFeedItems.collectLatest(pagingDataPresenter::collectFrom) }
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
