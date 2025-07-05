package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.domain.model.TokenCarouselItem

interface NewsFeedComponent {
  val onTokenCarouselItemClick: (List<TokenCarouselItem>, String) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (List<TokenCarouselItem>, String) -> Unit,
    ): NewsFeedComponent
  }
}
