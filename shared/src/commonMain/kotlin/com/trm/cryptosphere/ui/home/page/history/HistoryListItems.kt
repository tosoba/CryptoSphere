package com.trm.cryptosphere.ui.home.page.history

import com.trm.cryptosphere.domain.model.NewsHistoryItem
import com.trm.cryptosphere.domain.model.TokenHistoryItem
import kotlinx.datetime.LocalDate

sealed interface NewsHistoryListItem {
  data class Item(val data: NewsHistoryItem) : NewsHistoryListItem

  data class DateHeader(val date: LocalDate) : NewsHistoryListItem
}

sealed interface TokenHistoryListItem {
  data class Item(val data: TokenHistoryItem) : TokenHistoryListItem

  data class DateHeader(val date: LocalDate) : TokenHistoryListItem
}
