package com.trm.cryptosphere.ui.home.page.prices

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
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

  val tokens: Flow<PagingData<TokenItem>> =
    query.debounce(250).flatMapLatest(tokenRepository::getTokens).cachedIn(scope)

  fun onQueryChange(newQuery: String) {
    query.value = newQuery.trim()
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
