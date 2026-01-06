package com.trm.cryptosphere.core.base

import androidx.paging.CombinedLoadStates
import androidx.paging.PagingData
import androidx.paging.PagingDataEvent
import androidx.paging.PagingDataPresenter
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

actual class PagingItemsState<Item : Any>
actual constructor(scope: CoroutineScope, useCase: () -> Flow<PagingData<Item>>) {
  private val pagingDataPresenter =
    object : PagingDataPresenter<Item>() {
      override suspend fun presentPagingDataEvent(event: PagingDataEvent<Item>) {
        _itemsSnapshotList.value = PagingItemsSnapshotList(snapshot())
      }
    }

  private val _itemsSnapshotList: MutableStateFlow<PagingItemsSnapshotList<Item>> =
    MutableStateFlow(PagingItemsSnapshotList(pagingDataPresenter.snapshot()))
  @Suppress("unused") // Used in Swift
  val itemsSnapshotList: StateFlow<PagingItemsSnapshotList<Item>> = _itemsSnapshotList.asStateFlow()

  @Suppress("unused") // Used in Swift
  val loadStates: StateFlow<CombinedLoadStates?> = pagingDataPresenter.loadStateFlow

  init {
    scope.launch { useCase().cachedIn(scope).collectLatest(pagingDataPresenter::collectFrom) }
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
