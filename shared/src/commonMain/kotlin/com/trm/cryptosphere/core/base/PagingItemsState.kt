package com.trm.cryptosphere.core.base

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

expect class PagingItemsState<Item : Any>(
  scope: CoroutineScope,
  useCase: () -> Flow<PagingData<Item>>,
)
