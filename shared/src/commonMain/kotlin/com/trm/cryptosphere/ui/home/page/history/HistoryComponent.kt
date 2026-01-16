package com.trm.cryptosphere.ui.home.page.history

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface HistoryComponent : ComponentContext {
  val viewModel: HistoryViewModel

  val onTokenClick: (Int, TokenCarouselConfig) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenClick: (Int, TokenCarouselConfig) -> Unit,
    ): HistoryComponent
  }
}
