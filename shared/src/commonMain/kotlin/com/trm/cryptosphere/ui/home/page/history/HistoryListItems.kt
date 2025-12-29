package com.trm.cryptosphere.ui.home.page.history

import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.TokenHistoryItem

sealed interface NewsHistoryListItem {
  data class Item(val news: NewsHistoryItem) : NewsHistoryListItem

  data class DateHeader(val date: String) : NewsHistoryListItem
}

sealed interface TokenHistoryListItem {
  data class Item(val token: TokenHistoryItem) : TokenHistoryListItem

  data class DateHeader(val date: String) : TokenHistoryListItem
}
