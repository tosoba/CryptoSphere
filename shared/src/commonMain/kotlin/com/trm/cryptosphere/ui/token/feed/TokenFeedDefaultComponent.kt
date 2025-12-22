package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.statekeeper.ExperimentalStateKeeperApi
import com.arkivanov.essenty.statekeeper.saveable
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.repository.HistoryRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  private val mode: TokenFeedMode,
  override val tokenCarouselConfig: TokenCarouselConfig,
  override val navigateBack: () -> Unit,
  override val navigateHome: () -> Unit,
  private val navigateToTokenFeed: (TokenFeedMode, TokenCarouselConfig) -> Unit,
  tokenRepository: TokenRepository,
  private val historyRepository: HistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : TokenFeedComponent, ComponentContext by componentContext {
  @OptIn(ExperimentalStateKeeperApi::class)
  override val viewState: TokenFeedViewState by
    saveable(serializer = Long.serializer().nullable, state = { it.historyId.value.valueOrNull }) {
      historyId ->
      retainedInstance {
        TokenFeedViewState(
          historyId = historyId,
          mode = mode,
          tokenRepository = tokenRepository,
          historyRepository = historyRepository,
          dispatchers = dispatchers,
        )
      }
    }

  override fun navigateToTokenFeed(tokenId: Int) {
    if (viewState.mode.tokenId == tokenId) return
    viewState.historyId.value.valueOrNull?.let {
      navigateToTokenFeed(
        TokenFeedMode.HistoryContinuation(
          historyId = it,
          previousTokenId = mode.tokenId,
          tokenId = tokenId,
        ),
        tokenCarouselConfig,
      )
    }
  }
}
