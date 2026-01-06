package com.trm.cryptosphere.ui.home.page.prices

import com.arkivanov.decompose.ComponentContext

interface PricesComponent {
  val viewModel: PricesViewModel

  fun interface Factory {
    operator fun invoke(componentContext: ComponentContext): PricesComponent
  }
}
