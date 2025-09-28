package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.root.RootComponent.Child.Home
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenDetails
import com.trm.cryptosphere.ui.root.RootComponent.Child.TokenFeed
import com.trm.cryptosphere.ui.token.details.TokenDetailsComponent
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import kotlinx.serialization.Serializable

class RootDefaultComponent(
  componentContext: ComponentContext,
  private val homeComponentFactory: HomeComponent.Factory,
  private val tokenFeedComponentFactory: TokenFeedComponent.Factory,
  private val tokenDetailsComponentFactory: TokenDetailsComponent.Factory,
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
            onTokenCarouselItemClick = ::navigateToTokenFeed,
          )
        )
      }
      is ChildConfig.TokenFeed -> {
        TokenFeed(
          tokenFeedComponentFactory(
            componentContext = componentContext,
            mainTokenId = config.mainTokenId,
            tokenCarouselConfig = config.tokenCarouselConfig,
            navigateToTokenDetails = ::navigateToTokenDetails,
          )
        )
      }
      is ChildConfig.TokenDetails -> {
        TokenDetails(
          tokenDetailsComponentFactory(
            componentContext = componentContext,
            tokenId = config.tokenId,
          )
        )
      }
    }

  private fun navigateToTokenFeed(mainTokenId: Int, tokenCarouselConfig: TokenCarouselConfig) {
    navigation.pushNew(ChildConfig.TokenFeed(mainTokenId, tokenCarouselConfig))
  }

  private fun navigateToTokenDetails(id: Int) {
    navigation.pushNew(ChildConfig.TokenDetails(id))
  }

  @Serializable
  private sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable
    data class TokenFeed(val mainTokenId: Int, val tokenCarouselConfig: TokenCarouselConfig) :
      ChildConfig

    @Serializable data class TokenDetails(val tokenId: Int) : ChildConfig
  }
}
