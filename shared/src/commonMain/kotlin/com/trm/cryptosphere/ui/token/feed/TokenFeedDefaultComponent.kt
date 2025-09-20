package com.trm.cryptosphere.ui.token.feed

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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.builtins.serializer

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  initialMainTokenSymbol: String,
  override val tokenCarouselConfig: TokenCarouselConfig,
  override val navigateToTokenDetails: (String) -> Unit,
  private val tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
) : TokenFeedComponent, ComponentContext by componentContext {
  private val scope = coroutineScope(dispatchers.main + SupervisorJob())

  override val mainTokenSymbol: StateFlowInstance<String> by
    @OptIn(ExperimentalStateKeeperApi::class)
    saveable(serializer = String.serializer(), state = { it.state.value }) { savedState ->
      retainedInstance {
        savedState?.let { StateFlowInstance(it) } ?: StateFlowInstance(initialMainTokenSymbol)
      }
    }

  @OptIn(ExperimentalCoroutinesApi::class)
  override val tokens: StateFlow<List<TokenItem>> =
    mainTokenSymbol.state
      .mapLatest(tokenRepository::getTokensBySharedTags)
      .stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = emptyList(),
      )
}
