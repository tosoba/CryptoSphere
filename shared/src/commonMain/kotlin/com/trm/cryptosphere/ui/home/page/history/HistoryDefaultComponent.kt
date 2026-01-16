package com.trm.cryptosphere.ui.home.page.history

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository

class HistoryDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenClick: (Int, TokenCarouselConfig) -> Unit,
  private val newsHistoryRepository: NewsHistoryRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  private val dispatchers: AppCoroutineDispatchers,
) : HistoryComponent, ComponentContext by componentContext {
  override val viewModel: HistoryViewModel = retainedInstance {
    HistoryViewModel(
      newsHistoryRepository = newsHistoryRepository,
      tokenHistoryRepository = tokenHistoryRepository,
      dispatchers = dispatchers,
    )
  }
}
