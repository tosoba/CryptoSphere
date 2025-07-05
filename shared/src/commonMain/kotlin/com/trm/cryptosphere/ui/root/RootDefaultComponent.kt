package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.domain.model.TokenCarouselItem
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.root.RootComponent.Child.*
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
            mainTokenSymbol = config.mainTokenSymbol,
            tokenCarouselItems = config.tokenCarouselItems,
          )
        )
      }
      is ChildConfig.TokenDetails -> {
        TokenDetails(
          tokenDetailsComponentFactory(componentContext = componentContext, symbol = config.symbol)
        )
      }
    }

  private fun navigateToTokenFeed(
    mainTokenSymbol: String,
    tokenCarouselItems: List<TokenCarouselItem>,
  ) {
    navigation.pushNew(ChildConfig.TokenFeed(mainTokenSymbol, tokenCarouselItems))
  }

  @Serializable
  private sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable
    data class TokenFeed(
      val mainTokenSymbol: String,
      val tokenCarouselItems: List<TokenCarouselItem>,
    ) : ChildConfig

    @Serializable data class TokenDetails(val symbol: String) : ChildConfig
  }
}
