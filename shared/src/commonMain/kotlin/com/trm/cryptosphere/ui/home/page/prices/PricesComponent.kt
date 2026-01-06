package com.trm.cryptosphere.ui.home.page.prices

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface PricesComponent {
  val viewModel: PricesViewModel
  val onTokenClick: (Int, TokenCarouselConfig) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenClick: (Int, TokenCarouselConfig) -> Unit,
    ): PricesComponent
  }
}
