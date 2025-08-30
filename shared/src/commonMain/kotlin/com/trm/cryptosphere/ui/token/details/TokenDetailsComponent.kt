package com.trm.cryptosphere.ui.token.details

import com.arkivanov.decompose.ComponentContext

interface TokenDetailsComponent {
  val symbol: String

  fun interface Factory {
    operator fun invoke(componentContext: ComponentContext, symbol: String): TokenDetailsComponent
  }
}
