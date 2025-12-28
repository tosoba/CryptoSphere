package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import com.trm.cryptosphere.domain.usecase.GetNewsFeedUseCase

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenCarouselItemClick: (Int, TokenCarouselConfig) -> Unit,
  private val getNewsFeedUseCase: GetNewsFeedUseCase,
  private val newsHistoryRepository: NewsHistoryRepository,
  private val dispatchers: AppCoroutineDispatchers,
) : NewsFeedComponent, ComponentContext by componentContext {
  override val viewModel: NewsFeedViewModel = retainedInstance {
    NewsFeedViewModel(getNewsFeedUseCase, newsHistoryRepository, dispatchers)
  }

  override fun onLinkClick(news: NewsItem) {
    viewModel.onLinkClick(news)
  }
}
