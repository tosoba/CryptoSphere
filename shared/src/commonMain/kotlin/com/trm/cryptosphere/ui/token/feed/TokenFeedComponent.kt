package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext

interface TokenFeedComponent {
  fun interface Factory {
    operator fun invoke(componentContext: ComponentContext, symbol: String): TokenFeedComponent
  }
}
