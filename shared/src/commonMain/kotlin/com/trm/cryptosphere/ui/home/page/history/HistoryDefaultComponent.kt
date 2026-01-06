package com.trm.cryptosphere.ui.home.page.history

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.repository.NewsHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository

class HistoryDefaultComponent(
  componentContext: ComponentContext,
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

  override fun onDeleteHistoryClick(page: HistoryPage) {
    when (page) {
      HistoryPage.NEWS -> viewModel.deleteAllNewsHistory()
      HistoryPage.TOKENS -> viewModel.deleteAllTokenHistory()
    }
  }

  override fun onDeleteNewsHistory(id: Long) {
    viewModel.deleteNewsHistory(id)
  }

  override fun onDeleteTokenHistory(id: Long) {
    viewModel.deleteTokenHistory(id)
  }

  override fun onQueryChange(query: String) {
    viewModel.onQueryChange(query)
  }
}
