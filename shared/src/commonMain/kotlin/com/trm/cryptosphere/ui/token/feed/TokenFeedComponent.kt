package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface TokenFeedComponent {
  val mainTokenSymbol: String
  val tokenCarouselConfig: TokenCarouselConfig
  val tokenFeedItems: List<String>
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
