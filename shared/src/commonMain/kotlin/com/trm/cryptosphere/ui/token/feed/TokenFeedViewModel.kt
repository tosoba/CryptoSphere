package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow

class TokenFeedViewModel(
  tokenId: Int,
  tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())
  val tokens: Flow<PagingData<TokenItem>> =
    tokenRepository.getTokensBySharedTags(tokenId).cachedIn(scope)

  override fun onDestroy() {
    scope.cancel()
  }
}
