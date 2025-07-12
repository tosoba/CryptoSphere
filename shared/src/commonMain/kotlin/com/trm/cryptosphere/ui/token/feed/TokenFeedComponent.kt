package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import kotlinx.coroutines.flow.StateFlow

interface TokenFeedComponent {
  val state: StateFlow<TokenFeedState>
  val tokenCarouselConfig: TokenCarouselConfig
  val navigateToTokenDetails: (String) -> Unit

  fun reloadFeedForSymbol(symbol: String)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mainTokenSymbol: String,
      tokenCarouselConfig: TokenCarouselConfig,
      navigateToTokenDetails: (String) -> Unit,
    ): TokenFeedComponent
  }
}
