package com.trm.cryptosphere.core.base

import androidx.paging.PagingData
import androidx.paging.cachedIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

actual class PagingItemsState<Item : Any>
actual constructor(scope: CoroutineScope, useCase: () -> Flow<PagingData<Item>>) {
  val flow: Flow<PagingData<Item>> = useCase().cachedIn(scope)
}
