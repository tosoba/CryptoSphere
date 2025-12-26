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
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
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

  private var currentTokenId: Int = tokenId

  init {
    stack.subscribe(lifecycle) {
      viewModel.updateTokenIds(it.items.map { (config) -> config.tokenId })
    }
  }

  override fun onBackClicked() {
    navigation.pop()
  }

  private fun createChild(
    config: TokenFeedConfig,
    componentContext: ComponentContext,
  ): TokenNavigationComponent.TokenFeedChild =
    TokenNavigationComponent.TokenFeedChild(
      tokenFeedComponentFactory(
        componentContext = componentContext,
        tokenId = config.tokenId,
        onCurrentTokenChange = { it?.let { currentTokenId = it.id } },
      )
    )

  override fun navigateToTokenFeed() {
    val (currentFeedConfig) = stack.value.items.last()
    if (currentFeedConfig.tokenId == currentTokenId) return

    viewModel.addTokenToHistory(currentTokenId)
    navigation.pushToFront(TokenFeedConfig(currentTokenId))
  }

  override fun popToToken(token: TokenItem) {
    navigation.popWhile { (tokenId) -> tokenId != token.id }
  }

  @Serializable data class TokenFeedConfig(val tokenId: Int)
}

class TokenNavigationViewModel(
  initialTokenIds: List<Int>,
  private val tokenRepository: TokenRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val _tokenIds: MutableStateFlow<List<Int>> = MutableStateFlow(initialTokenIds)
  val currentTokenId: Flow<Int?> = _tokenIds.map { it.lastOrNull() }
  val tokens: Flow<List<TokenItem>> =
    _tokenIds.map {
      val tokens = tokenRepository.getTokensByIds(it).associateBy(TokenItem::id)
      it.mapNotNull { tokenId -> tokens[tokenId] }
    }

  fun updateTokenIds(tokenIds: List<Int>) {
    _tokenIds.value = tokenIds
  }

  fun addTokenToHistory(tokenId: Int) {
    scope.launch { tokenHistoryRepository.addTokenToHistory(tokenId) }
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
