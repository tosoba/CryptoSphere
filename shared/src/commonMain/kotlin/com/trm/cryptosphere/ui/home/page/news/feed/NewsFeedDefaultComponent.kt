package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.usecase.GetNewsUseCase
import kotlinx.coroutines.flow.Flow

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  private val getNewsUseCase: GetNewsUseCase,
  private val dispatchers: AppCoroutineDispatchers,
  override val onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
) : NewsFeedComponent, ComponentContext by componentContext {
  override val state: Flow<PagingData<NewsItem>> =
    instanceKeeper.getOrCreate { NewsFeedViewState(getNewsUseCase, dispatchers) }.value
}
