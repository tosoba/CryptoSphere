package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig

interface NewsFeedComponent {
  val onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
    ): NewsFeedComponent
  }
}
