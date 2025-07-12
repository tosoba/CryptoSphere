package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenCarouselItem
import com.trm.cryptosphere.domain.model.mockTokenCarouselItems
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  mainTokenSymbol: String,
  override val tokenCarouselConfig: TokenCarouselConfig,
  override val navigateToTokenDetails: (String) -> Unit,
) : TokenFeedComponent, ComponentContext by componentContext {
  private val stateHolder by
    @OptIn(ExperimentalStateKeeperApi::class)
    saveable(serializer = TokenFeedState.serializer(), state = { it.state.value }) { savedState ->
      retainedInstance { savedState?.let(::StateHolder) ?: StateHolder(mainTokenSymbol) }
    }

  override val state: StateFlow<TokenFeedState> = stateHolder.state

  override fun reloadFeedForSymbol(symbol: String) {
    stateHolder.updateMainTokenSymbol(symbol)
  }

  private class StateHolder(state: TokenFeedState) : InstanceKeeper.Instance {
    constructor(
      mainTokenSymbol: String
    ) : this(TokenFeedState(mainTokenSymbol, feedItems(mainTokenSymbol)))

    private val _state = MutableStateFlow(state)
    val state = _state.asStateFlow()

    fun updateMainTokenSymbol(symbol: String) {
      _state.update { it.copy(mainTokenSymbol = symbol, feedItems = feedItems(symbol)) }
    }

    companion object {
      // TODO: feed items will be retrieved from local data sources
      private fun feedItems(symbol: String): List<String> = buildList {
        add(symbol)
        addAll(mockTokenCarouselItems().map(TokenCarouselItem::symbol).filter { it != symbol })
      }
    }
  }
}
