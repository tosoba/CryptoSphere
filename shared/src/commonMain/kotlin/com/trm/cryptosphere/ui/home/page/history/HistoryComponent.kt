package com.trm.cryptosphere.ui.home.page.history

import com.arkivanov.decompose.ComponentContext

interface HistoryComponent : ComponentContext {
  val viewModel: HistoryViewModel

  fun onDeleteHistoryClick(page: HistoryPage)

  fun onQueryChange(query: String)

  fun interface Factory {
    operator fun invoke(componentContext: ComponentContext): HistoryComponent
  }
}
