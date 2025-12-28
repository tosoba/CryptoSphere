package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem

interface NewsFeedComponent : ComponentContext {
  val viewModel: NewsFeedViewModel
  val onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit

  fun onLinkClick(news: NewsItem)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit,
    ): NewsFeedComponent
  }
}
