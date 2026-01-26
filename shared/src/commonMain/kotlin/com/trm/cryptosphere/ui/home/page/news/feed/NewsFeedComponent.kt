package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface NewsFeedComponent : ComponentContext {
  val viewModel: NewsFeedViewModel
  val onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit
  val onSeedImageUrlChange: (String?) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit,
      onSeedImageUrlChange: (String?) -> Unit,
    ): NewsFeedComponent
  }
}
