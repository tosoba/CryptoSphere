package com.trm.cryptosphere.ui.home.page.history

import com.trm.cryptosphere.shared.MR
import dev.icerock.moko.resources.StringResource

enum class HistoryPage(val index: Int, val labelRes: StringResource) {
  NEWS(index = 0, labelRes = MR.strings.news),
  TOKENS(index = 1, labelRes = MR.strings.tokens);

  companion object {
    fun fromIndex(index: Int): HistoryPage = entries.first { it.index == index }
  }
}
