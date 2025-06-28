package com.trm.cryptosphere.ui.token

import com.arkivanov.decompose.ComponentContext

interface TokenComponent {
  fun interface Factory {
    operator fun invoke(componentContext: ComponentContext, symbol: String): TokenComponent
  }
}
