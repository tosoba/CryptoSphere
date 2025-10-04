@file:Suppress("unused") // Used in Swift

package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.ItemSnapshotList
import com.trm.cryptosphere.domain.model.NewsItem

data class NewsItemsSnapshotList(val items: ItemSnapshotList<NewsItem>) {
  fun itemAt(index: Int): NewsItem? = if (items.size <= index) null else items[index]
}
