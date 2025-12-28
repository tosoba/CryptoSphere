package com.trm.cryptosphere.domain.model

sealed interface HistoryListItem {
  data class Separator(val date: String) : HistoryListItem
}

sealed interface NewsHistoryListItem : HistoryListItem {
  data class Item(val news: NewsHistoryItem) : NewsHistoryListItem

  data class Separator(val date: String) : NewsHistoryListItem
}

sealed interface TokenHistoryListItem : HistoryListItem {
  data class Item(val token: TokenHistoryItem) : TokenHistoryListItem

  data class Separator(val date: String) : TokenHistoryListItem
}
