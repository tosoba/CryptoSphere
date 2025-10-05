package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.usecase.GetNewsFeedUseCase

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit,
  private val getNewsFeedUseCase: GetNewsFeedUseCase,
  private val dispatchers: AppCoroutineDispatchers,
) : NewsFeedComponent, ComponentContext by componentContext {
  override val viewState: NewsFeedViewState =
    instanceKeeper.getOrCreate { NewsFeedViewState(getNewsFeedUseCase, dispatchers) }
}
