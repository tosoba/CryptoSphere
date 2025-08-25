package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.base.LoadableState
import com.trm.cryptosphere.core.base.RestartableStateFlow
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem

interface NewsFeedComponent {
  val state: RestartableStateFlow<LoadableState<List<NewsItem>>>
  val onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
    ): NewsFeedComponent
  }
}
