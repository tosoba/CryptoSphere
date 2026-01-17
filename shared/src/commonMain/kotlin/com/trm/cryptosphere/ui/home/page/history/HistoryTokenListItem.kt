package com.trm.cryptosphere.ui.home.page.history

import com.trm.cryptosphere.domain.model.TokenHistoryItem
import kotlinx.datetime.LocalDate

sealed interface HistoryTokenListItem {
  data class Item(val data: TokenHistoryItem) : HistoryTokenListItem

  data class DateHeader(val date: LocalDate) : HistoryTokenListItem

  val key: Long
    get() =
      when (this) {
        is Item -> data.id
        is DateHeader -> date.toEpochDays()
      }
}
