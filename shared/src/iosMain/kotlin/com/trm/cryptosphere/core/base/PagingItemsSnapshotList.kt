package com.trm.cryptosphere.core.base

import androidx.paging.ItemSnapshotList

data class PagingItemsSnapshotList<T : Any>(private val list: ItemSnapshotList<T>) :
  List<T> by list.items
