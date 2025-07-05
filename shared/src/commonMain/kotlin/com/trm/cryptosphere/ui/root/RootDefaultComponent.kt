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
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
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
        RootComponent.Child.Home(
          homeComponentFactory(
            componentContext = componentContext,
            onTokenCarouselItemClick = { items, currentIndex ->
              navigateToTokenFeed(
                symbols = items.map(TokenCarouselItem::symbol),
                currentIndex = currentIndex,
              )
            },
          )
        )
      }
      is ChildConfig.TokenFeed -> {
        RootComponent.Child.TokenFeed(
          tokenFeedComponentFactory(
            componentContext = componentContext,
            symbols = config.symbols,
            currentIndex = config.currentIndex,
          )
        )
      }
    }

  private fun navigateToTokenFeed(symbols: List<String>, currentIndex: Int) {
    navigation.pushNew(ChildConfig.TokenFeed(symbols, currentIndex))
  }

  @Serializable
  private sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable
    data class TokenFeed(val symbols: List<String>, val currentIndex: Int) : ChildConfig
  }
}
