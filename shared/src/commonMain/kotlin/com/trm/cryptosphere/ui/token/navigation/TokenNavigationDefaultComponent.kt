package com.trm.cryptosphere.ui.token.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popWhile
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.subscribe
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import kotlinx.serialization.Serializable

class TokenNavigationDefaultComponent(
  componentContext: ComponentContext,
  tokenId: Int,
  override val tokenCarouselConfig: TokenCarouselConfig,
  private val tokenFeedComponentFactory: TokenFeedComponent.Factory,
  private val tokenRepository: TokenRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
  override val navigateHome: () -> Unit,
) : TokenNavigationComponent, ComponentContext by componentContext {
  private val navigation = StackNavigation<TokenFeedConfig>()

  override val stack: Value<ChildStack<TokenFeedConfig, TokenNavigationComponent.TokenFeedChild>> =
    childStack(
      source = navigation,
      serializer = TokenFeedConfig.serializer(),
      initialConfiguration = TokenFeedConfig(tokenId),
      handleBackButton = true,
      childFactory = ::createChild,
    )

  override val viewModel = retainedInstance {
    TokenNavigationViewModel(
      initialTokenIds = stack.value.items.map { (config) -> config.tokenId },
      tokenRepository = tokenRepository,
      tokenHistoryRepository = tokenHistoryRepository,
      dispatchers = dispatchers,
    )
  }

  private var currentFeedTokenId: Int = tokenId

  init {
    stack.subscribe(lifecycle) {
      viewModel.updateTokenIds(it.items.map { (config) -> config.tokenId })
    }
  }

  override fun onBackClicked() {
    if (stack.value.items.size > 1) navigation.pop() else navigateHome()
  }

  private fun createChild(
    config: TokenFeedConfig,
    componentContext: ComponentContext,
  ): TokenNavigationComponent.TokenFeedChild =
    TokenNavigationComponent.TokenFeedChild(
      tokenFeedComponentFactory(
        componentContext = componentContext,
        tokenId = config.tokenId,
        onCurrentFeedTokenChange = { it?.let { currentFeedTokenId = it.id } },
      )
    )

  override fun navigateToTokenFeed() {
    val (currentFeedConfig) = stack.value.items.last()
    if (currentFeedConfig.tokenId == currentFeedTokenId) return

    viewModel.addTokenToHistory(currentFeedTokenId)
    navigation.pushToFront(TokenFeedConfig(currentFeedTokenId))
  }

  override fun popToToken(token: TokenItem) {
    navigation.popWhile { (tokenId) -> tokenId != token.id }
  }

  @Serializable data class TokenFeedConfig(val tokenId: Int)
}
