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
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenNavigation
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationComponent
import kotlinx.serialization.Serializable

class RootDefaultComponent(
  componentContext: ComponentContext,
  private val homeComponentFactory: HomeComponent.Factory,
  private val tokenNavigationComponentFactory: TokenNavigationComponent.Factory,
) : RootComponent, ComponentContext by componentContext {
  private val navigation = StackNavigation<ChildConfig>()

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
            onTokenCarouselItemClick = ::navigateToTokenFeed,
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
    }

  private fun navigateToTokenFeed(tokenId: Int, tokenCarouselConfig: TokenCarouselConfig) {
    navigation.pushToFront(ChildConfig.TokenNavigation(tokenId, tokenCarouselConfig))
  }

  @Serializable
  sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable
    data class TokenNavigation(val tokenId: Int, val tokenCarouselConfig: TokenCarouselConfig) :
      ChildConfig
  }
}
