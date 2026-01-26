package com.trm.cryptosphere.ui.token.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import kotlinx.coroutines.flow.StateFlow

interface TokenNavigationComponent : BackHandlerOwner {
  val tokenCarouselConfig: TokenCarouselConfig
  val currentPresentedFeedToken: StateFlow<TokenItem?>
  val navigateHome: () -> Unit
  val stack: Value<ChildStack<*, TokenFeedChild>>
  val viewModel: TokenNavigationViewModel

  fun onBackClicked()

  fun navigateToTokenFeed()

  fun popToToken(token: TokenItem)

  class TokenFeedChild(val component: TokenFeedComponent)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      tokenId: Int,
      tokenCarouselConfig: TokenCarouselConfig,
      navigateHome: () -> Unit,
      onSeedImageUrlChange: (String?) -> Unit,
    ): TokenNavigationComponent
  }
}
