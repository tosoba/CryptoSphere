package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.domain.model.TokenCarouselItem

interface TokenFeedComponent {
  val tokenCarouselItems: List<TokenCarouselItem>
  val tokenFeedItems: List<String>

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mainTokenSymbol: String,
      tokenCarouselItems: List<TokenCarouselItem>,
    ): TokenFeedComponent
  }
}
