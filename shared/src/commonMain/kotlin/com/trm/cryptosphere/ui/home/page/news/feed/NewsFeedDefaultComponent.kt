package com.trm.cryptosphere.ui.home.page.news.feed

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.usecase.GetNewsUseCase
import com.trm.cryptosphere.domain.usecase.GetTokensRelatedToNewsItemUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit,
  private val getNewsUseCase: GetNewsUseCase,
  private val getTokensRelatedToNewsItemUseCase: GetTokensRelatedToNewsItemUseCase,
  private val dispatchers: AppCoroutineDispatchers,
) : NewsFeedComponent, ComponentContext by componentContext {
  private val scope = coroutineScope(dispatchers.main + SupervisorJob())

  override val newsItems: Flow<PagingData<NewsItem>> =
    instanceKeeper.getOrCreate { NewsFeedViewState(getNewsUseCase, dispatchers) }.value

  private val currentNewsItem = MutableSharedFlow<NewsItem>()

  @OptIn(ExperimentalCoroutinesApi::class)
  override val relatedTokens: StateFlow<List<TokenItem>> =
    currentNewsItem
      .mapLatest(getTokensRelatedToNewsItemUseCase::invoke)
      .stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList(),
      )

  override fun onCurrentItemChanged(item: NewsItem) {
    scope.launch { currentNewsItem.emit(item) }
  }
}
