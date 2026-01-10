package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingConfig
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.PagingItemsState
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel

class TokenFeedViewModel(
  tokenId: Int,
  tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  val tokensPagingState: PagingItemsState<TokenItem> =
    PagingItemsState(scope) {
      tokenRepository.getTokensBySharedTags(
        id = tokenId,
        config =
          PagingConfig(
            pageSize = PAGE_SIZE,
            prefetchDistance = PREFETCH_DISTANCE,
            initialLoadSize = PAGE_SIZE,
          ),
      )
    }

  override fun onDestroy() {
    scope.cancel()
  }

  companion object {
    private const val PAGE_SIZE = 100
    const val PREFETCH_DISTANCE = 5
  }
}
