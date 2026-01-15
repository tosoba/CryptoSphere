package com.trm.cryptosphere.ui.home.page.prices

import androidx.paging.PagingConfig
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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class PricesViewModel(
  private val tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val _query = MutableStateFlow("")
  val query: StateFlow<String> = _query.asStateFlow()

  val tokensPagingState =
    PagingItemsState(scope) {
      _query.debounce(250).flatMapLatest {
        tokenRepository.getTokens(
          query = it,
          config = PagingConfig(pageSize = PAGE_SIZE, initialLoadSize = PAGE_SIZE),
        )
      }
    }

  fun onQueryChange(newQuery: String) {
    _query.value = newQuery.trim()
  }

  override fun onDestroy() {
    scope.cancel()
  }

  companion object {
    const val PAGE_SIZE = 100
  }
}
