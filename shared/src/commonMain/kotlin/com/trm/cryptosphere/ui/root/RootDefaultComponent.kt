package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.root.RootComponent.Child.Home
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenFeed
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenNavigation
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationComponent
import kotlinx.serialization.Serializable

class RootDefaultComponent(
  componentContext: ComponentContext,
  private val homeComponentFactory: HomeComponent.Factory,
  private val tokenNavigationComponentFactory: TokenNavigationComponent.Factory,
  private val tokenFeedComponentFactory: TokenFeedComponent.Factory,
  private val tokenHistoryRepository: TokenHistoryRepository,
  private val dispatchers: AppCoroutineDispatchers,
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

  override fun onBackClicked() {
    navigation.pop()
  }

  override fun onBackClicked(toIndex: Int) {
    navigation.popTo(toIndex)
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
          )
        )
      }
      is ChildConfig.TokenFeed -> {
        TokenFeed(
          tokenFeedComponentFactory(
            componentContext = componentContext,
            tokenId = config.tokenId,
            onCurrentFeedTokenChange = {},
          )
        )
      }
    }

  private fun navigateToTokenFeed(tokenId: Int, tokenCarouselConfig: TokenCarouselConfig) {
    viewModel.onTokenSelected(tokenId)
    navigation.pushToFront(ChildConfig.TokenNavigation(tokenId, tokenCarouselConfig))
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
