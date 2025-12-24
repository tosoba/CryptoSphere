package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenRepository

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  private val mode: TokenFeedMode,
  override val tokenCarouselConfig: TokenCarouselConfig,
  override val navigateBack: () -> Unit,
  override val navigateBackToIndex: (Int) -> Unit,
  override val navigateHome: () -> Unit,
  private val navigateToTokenFeed: (TokenFeedMode, TokenCarouselConfig) -> Unit,
  tokenRepository: TokenRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : TokenFeedComponent, ComponentContext by componentContext {
  override val viewState: TokenFeedViewState =
    instanceKeeper.getOrCreate {
      TokenFeedViewState(
        mode = mode,
        tokenRepository = tokenRepository,
        tokenHistoryRepository = tokenHistoryRepository,
        dispatchers = dispatchers,
      )
    }

  override fun navigateToTokenFeed(tokenId: Int) {
    if (mode.tokenId == tokenId) return

    viewState.historyState.value.valueOrNull?.let {
      navigateToTokenFeed(
        TokenFeedMode.HistoryContinuation(
          previousTokenIds =
            buildList {
              when (mode) {
                is TokenFeedMode.HistoryFirst -> {
                  add(mode.tokenId)
                }
                is TokenFeedMode.HistoryContinuation -> {
                  addAll(mode.previousTokenIds)
                  add(mode.tokenId)
                }
              }
            },
          tokenId = tokenId,
        ),
        tokenCarouselConfig,
      )
    }
  }
}
