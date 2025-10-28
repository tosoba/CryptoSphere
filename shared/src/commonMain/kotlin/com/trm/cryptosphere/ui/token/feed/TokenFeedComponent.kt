package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface TokenFeedComponent {
  val viewState: TokenFeedViewState
  val tokenCarouselConfig: TokenCarouselConfig
  val navigateBack: () -> Unit
  val navigateHome: () -> Unit
  val navigateToTokenDetails: (Int) -> Unit

  fun navigateToTokenFeed(tokenId: Int)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mode: TokenFeedMode,
      tokenCarouselConfig: TokenCarouselConfig,
      navigateBack: () -> Unit,
      navigateHome: () -> Unit,
      navigateToTokenFeed: (TokenFeedMode, TokenCarouselConfig) -> Unit,
      navigateToTokenDetails: (Int) -> Unit,
    ): TokenFeedComponent
  }
}
