package com.trm.cryptosphere.ui.home.page.history

import com.trm.cryptosphere.domain.model.NewsHistoryItem
import kotlinx.datetime.LocalDate

sealed interface HistoryNewsListItem {
  data class Item(val data: NewsHistoryItem) : HistoryNewsListItem

  data class DateHeader(val date: LocalDate) : HistoryNewsListItem

  val key: Long
    get() =
      when (this) {
        is Item -> data.id
        is DateHeader -> date.toEpochDays()
      }
}
