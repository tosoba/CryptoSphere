package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.domain.model.TokenCarouselItem

interface NewsFeedComponent {
  val onTokenCarouselItemClick: (String, List<TokenCarouselItem>) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (String, List<TokenCarouselItem>) -> Unit,
    ): NewsFeedComponent
  }
}
