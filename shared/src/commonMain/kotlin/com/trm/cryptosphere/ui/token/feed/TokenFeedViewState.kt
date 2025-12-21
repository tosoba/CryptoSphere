package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.LoadableState
import com.trm.cryptosphere.core.base.loadableStateFlowOf
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.HistoryRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn

class TokenFeedViewState(
  historyId: Long?,
  val mode: TokenFeedMode,
  tokenRepository: TokenRepository,
  private val historyRepository: HistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  val historyId: StateFlow<LoadableState<Long>> =
    flow {
        if (historyId == null) {
          emitAll(
            loadableStateFlowOf {
              when (mode) {
                is TokenFeedMode.HistoryFirst -> {
                  historyRepository.createNewHistory(mode.tokenId)
                }
                is TokenFeedMode.HistoryContinuation -> {
                  historyRepository
                    .addItemToHistory(
                      historyId = mode.historyId,
                      previousItemTokenId = mode.previousTokenId,
                      newItemTokenId = mode.tokenId,
                    )
                    .run { mode.historyId }
                }
              }
            }
          )
        } else {
          emit(LoadableState.Idle(historyId))
        }
      }
      .stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = LoadableState.Loading,
      )

  val tokens: Flow<PagingData<TokenItem>> =
    tokenRepository.getTokensBySharedTags(mode.tokenId).cachedIn(scope)

  override fun onDestroy() {
    scope.cancel()
  }
}
