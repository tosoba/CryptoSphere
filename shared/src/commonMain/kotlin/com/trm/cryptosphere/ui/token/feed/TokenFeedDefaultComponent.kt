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
  override val onCurrentFeedTokenChange: (TokenItem?) -> Unit,
) : TokenFeedComponent, ComponentContext by componentContext {
  override val viewModel = retainedInstance {
    TokenFeedViewModel(tokenId, tokenRepository, dispatchers)
  }
}
