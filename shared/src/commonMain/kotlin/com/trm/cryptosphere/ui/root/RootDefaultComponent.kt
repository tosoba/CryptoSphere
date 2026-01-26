package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.base.cancellableRunCatching
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.core.ui.theme.ColorExtractor
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.root.RootComponent.Child.Home
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenFeed
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenNavigation
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.serialization.Serializable

class RootDefaultComponent(
  componentContext: ComponentContext,
  private val homeComponentFactory: HomeComponent.Factory,
  private val tokenNavigationComponentFactory: TokenNavigationComponent.Factory,
  private val tokenFeedComponentFactory: TokenFeedComponent.Factory,
  private val tokenHistoryRepository: TokenHistoryRepository,
  private val dispatchers: AppCoroutineDispatchers,
  private val colorExtractor: ColorExtractor,
) : RootComponent, ComponentContext by componentContext {
  private val navigation = StackNavigation<ChildConfig>()

  private val viewModel: RootViewModel = retainedInstance {
    RootViewModel(tokenHistoryRepository, dispatchers)
  }

  override val stack: Value<ChildStack<ChildConfig, RootComponent.Child>> =
    childStack(
      source = navigation,
      serializer = ChildConfig.serializer(),
      initialConfiguration = ChildConfig.Home,
      handleBackButton = true,
      childFactory = ::createChild,
    )

  private val _seedImageUrl = MutableStateFlow<String?>(null)
  @OptIn(ExperimentalCoroutinesApi::class)
  override val colorExtractorResult: StateFlow<ColorExtractor.Result?> =
    _seedImageUrl
      .mapLatest {
        if (it != null) {
          cancellableRunCatching { colorExtractor.calculatePrimaryColor(it) }.getOrNull()
        } else {
          null
        }
      }
      .stateIn(
        scope = componentContext.coroutineScope(),
        started = SharingStarted.Lazily,
        initialValue = null,
      )

  override fun onBackClicked() {
    navigation.pop()
  }

  override fun onBackClicked(toIndex: Int) {
    navigation.popTo(toIndex)
  }

  override fun onSeedImageUrlChange(url: String?) {
    _seedImageUrl.value = url
  }

  private fun createChild(
    config: ChildConfig,
    componentContext: ComponentContext,
  ): RootComponent.Child =
    when (config) {
      ChildConfig.Home -> {
        Home(
          homeComponentFactory(
            componentContext = componentContext,
            onTokenClick = ::navigateToTokenFeed,
            onSeedImageUrlChange = ::onSeedImageUrlChange,
          )
        )
      }
      is ChildConfig.TokenNavigation -> {
        TokenNavigation(
          tokenNavigationComponentFactory(
            componentContext = componentContext,
            tokenId = config.tokenId,
            tokenCarouselConfig = config.tokenCarouselConfig,
            navigateHome = { onBackClicked(0) },
            onSeedImageUrlChange = ::onSeedImageUrlChange,
          )
        )
      }
      is ChildConfig.TokenFeed -> {
        TokenFeed(
          tokenFeedComponentFactory(
            componentContext = componentContext,
            tokenId = config.tokenId,
            onCurrentPresentedFeedTokenChange = {},
            navigateToTokenFeed = { token -> navigateToTokenFeed(token.id, null) },
            onSeedImageUrlChange = ::onSeedImageUrlChange,
          )
        )
      }
    }

  private fun navigateToTokenFeed(tokenId: Int, tokenCarouselConfig: TokenCarouselConfig?) {
    val config = stack.value.items.lastOrNull()?.configuration as? ChildConfig.TokenFeed
    if (config?.tokenId != tokenId) viewModel.onTokenSelected(tokenId)
    navigation.navigateToTokenFeed(tokenId, tokenCarouselConfig)
  }

  @Serializable
  sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable
    data class TokenNavigation(val tokenId: Int, val tokenCarouselConfig: TokenCarouselConfig) :
      ChildConfig

    @Serializable data class TokenFeed(val tokenId: Int) : ChildConfig
  }
}
