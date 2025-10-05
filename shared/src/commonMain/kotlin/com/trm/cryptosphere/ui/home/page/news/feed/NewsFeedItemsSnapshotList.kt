package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.ItemSnapshotList
import com.trm.cryptosphere.domain.model.NewsFeedItem

data class NewsFeedItemsSnapshotList(val items: ItemSnapshotList<NewsFeedItem>) :
  List<NewsFeedItem> by items.items
