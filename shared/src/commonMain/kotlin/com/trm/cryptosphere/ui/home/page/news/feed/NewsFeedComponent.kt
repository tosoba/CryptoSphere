package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsFeedComponent {
  val state: Flow<PagingData<NewsItem>>
  val onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
    ): NewsFeedComponent
  }
}
