package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  tokenId: Int,
  tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
  override val onCurrentPresentedFeedTokenChange: (TokenItem?) -> Unit,
  override val navigateToTokenFeed: (TokenItem) -> Unit,
  override val onSeedImageUrlChange: (String?) -> Unit,
) : TokenFeedComponent, ComponentContext by componentContext {
  override val viewModel = retainedInstance {
    TokenFeedViewModel(tokenId, tokenRepository, dispatchers)
  }
}
