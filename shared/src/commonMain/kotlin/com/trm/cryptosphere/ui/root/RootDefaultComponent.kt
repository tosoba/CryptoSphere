package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushToFront
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.root.RootComponent.Child.Home
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenFeed
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import com.trm.cryptosphere.ui.token.feed.TokenFeedMode
import kotlinx.serialization.Serializable

class RootDefaultComponent(
  componentContext: ComponentContext,
  private val homeComponentFactory: HomeComponent.Factory,
  private val tokenFeedComponentFactory: TokenFeedComponent.Factory,
) : RootComponent, ComponentContext by componentContext {
  private val navigation = StackNavigation<ChildConfig>()

  override val stack: Value<ChildStack<*, RootComponent.Child>> =
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
            onTokenCarouselItemClick = { tokenId, config ->
              navigateToTokenFeed(
                mode = TokenFeedMode.HistoryFirst(tokenId),
                tokenCarouselConfig = config,
              )
            },
          )
        )
      }
      is ChildConfig.TokenFeed -> {
        TokenFeed(
          tokenFeedComponentFactory(
            componentContext = componentContext,
            mode = config.mode,
            tokenCarouselConfig = config.tokenCarouselConfig,
            navigateBack = ::onBackClicked,
            navigateBackToIndex = ::onBackClicked,
            navigateHome = { onBackClicked(0) },
            navigateToTokenFeed = ::navigateToTokenFeed,
          )
        )
      }
    }

  private fun navigateToTokenFeed(mode: TokenFeedMode, tokenCarouselConfig: TokenCarouselConfig) {
    navigation.pushToFront(ChildConfig.TokenFeed(mode, tokenCarouselConfig))
  }

  @Serializable
  private sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable
    data class TokenFeed(val mode: TokenFeedMode, val tokenCarouselConfig: TokenCarouselConfig) :
      ChildConfig
  }
}
