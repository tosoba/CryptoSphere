package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.StateFlowInstance
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.serialization.builtins.serializer

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  initialMainTokenId: Int,
  override val tokenCarouselConfig: TokenCarouselConfig,
  override val navigateToTokenDetails: (Int) -> Unit,
  private val tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
) : TokenFeedComponent, ComponentContext by componentContext {
  private val scope = coroutineScope(dispatchers.main + SupervisorJob())

  override val mainTokenId: StateFlowInstance<Int> by
    @OptIn(ExperimentalStateKeeperApi::class)
    saveable(serializer = Int.serializer(), state = { it.state.value }) { savedState ->
      retainedInstance {
        savedState?.let { StateFlowInstance(it) } ?: StateFlowInstance(initialMainTokenId)
      }
    }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val tokens: Flow<PagingData<TokenItem>> =
    mainTokenId.state.flatMapLatest {
      tokenRepository.getTokensBySharedTags(it).cachedIn(scope)
    }
}
