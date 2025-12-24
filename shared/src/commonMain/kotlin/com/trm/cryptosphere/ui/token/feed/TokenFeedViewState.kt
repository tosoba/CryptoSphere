package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.LoadableState
import com.trm.cryptosphere.core.base.loadableStateFlowOf
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class TokenFeedViewState(
  val history: TokenFeedHistory,
  tokenRepository: TokenRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  val historyState: StateFlow<LoadableState<Unit>> =
    loadableStateFlowOf { tokenHistoryRepository.addTokenToHistory(history.tokenId) }
      .stateIn(
        scope = scope,
        started = SharingStarted.Eagerly,
        initialValue = LoadableState.Loading,
      )

  val tokens: Flow<PagingData<TokenItem>> =
    tokenRepository.getTokensBySharedTags(history.tokenId).cachedIn(scope)

  override fun onDestroy() {
    scope.cancel()
  }
}
