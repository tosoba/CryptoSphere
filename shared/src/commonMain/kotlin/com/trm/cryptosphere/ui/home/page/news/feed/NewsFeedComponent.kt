package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface NewsFeedComponent {
  val newsItems: Flow<PagingData<NewsItem>>
  val relatedTokens: StateFlow<List<TokenItem>>
  val onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit

  fun onCurrentItemChanged(item: NewsItem)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
    ): NewsFeedComponent
  }
}
