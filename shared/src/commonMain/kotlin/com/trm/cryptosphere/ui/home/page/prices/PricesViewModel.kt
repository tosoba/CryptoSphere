package com.trm.cryptosphere.ui.home.page.prices

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.PagingItemsState
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class PricesViewModel(
  private val tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val query = MutableStateFlow("")

  val tokensPagingState =
    PagingItemsState(scope) { query.debounce(250).flatMapLatest(tokenRepository::getTokens) }

  fun onQueryChange(newQuery: String) {
    query.value = newQuery.trim()
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
