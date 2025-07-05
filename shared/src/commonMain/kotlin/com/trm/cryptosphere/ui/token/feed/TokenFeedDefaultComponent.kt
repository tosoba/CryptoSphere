package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.domain.model.TokenCarouselItem
import com.trm.cryptosphere.domain.model.mockTokenCarouselItems

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  private val mainTokenSymbol: String,
  override val tokenCarouselItems: List<TokenCarouselItem>,
) : TokenFeedComponent, ComponentContext by componentContext {
  // TODO: this will be retrieved from local data sources
  override val tokenFeedItems: List<String> = buildList {
    add(mainTokenSymbol)
    addAll(mockTokenCarouselItems().map(TokenCarouselItem::symbol).filter { it != mainTokenSymbol })
  }
}
