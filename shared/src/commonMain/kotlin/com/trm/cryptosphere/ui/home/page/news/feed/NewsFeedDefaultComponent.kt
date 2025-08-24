package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.LoadableState
import com.trm.cryptosphere.core.base.RestartableStateFlow
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.usecase.GetNews

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  private val dispatchers: AppCoroutineDispatchers,
  private val getNews: GetNews,
  override val onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
) : NewsFeedComponent, ComponentContext by componentContext {
  val news: RestartableStateFlow<LoadableState<List<NewsItem>>> =
    instanceKeeper.getOrCreate { NewsFeedViewModel(dispatchers, getNews) }.value
}
