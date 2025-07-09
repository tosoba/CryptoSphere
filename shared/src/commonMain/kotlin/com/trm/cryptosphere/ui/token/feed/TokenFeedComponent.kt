package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface TokenFeedComponent {
  val tokenCarouselConfig: TokenCarouselConfig
  val tokenFeedItems: List<String>

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mainTokenSymbol: String,
      tokenCarouselConfig: TokenCarouselConfig,
    ): TokenFeedComponent
  }
}
