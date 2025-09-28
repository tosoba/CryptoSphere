package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedComponent {
  val viewState: NewsFeedViewState
  val relatedTokens: StateFlow<List<TokenItem>>
  val onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit

  fun onCurrentItemChanged(item: NewsItem)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit,
    ): NewsFeedComponent
  }
}
