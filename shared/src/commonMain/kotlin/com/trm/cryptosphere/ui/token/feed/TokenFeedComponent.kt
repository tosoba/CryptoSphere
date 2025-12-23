package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface TokenFeedComponent {
  val viewState: TokenFeedViewState
  val tokenCarouselConfig: TokenCarouselConfig
  val navigateBack: () -> Unit
  val navigateBackToIndex: (Int) -> Unit
  val navigateHome: () -> Unit

  fun navigateToTokenFeed(tokenId: Int)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mode: TokenFeedMode,
      tokenCarouselConfig: TokenCarouselConfig,
      navigateBack: () -> Unit,
      navigateBackToIndex: (Int) -> Unit,
      navigateHome: () -> Unit,
      navigateToTokenFeed: (TokenFeedMode, TokenCarouselConfig) -> Unit,
    ): TokenFeedComponent
  }
}
