package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.token.TokenComponent
import kotlinx.serialization.Serializable

class RootDefaultComponent(
  componentContext: ComponentContext,
  private val homeComponentFactory: HomeComponent.Factory,
  private val tokenComponentFactory: TokenComponent.Factory,
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
            onTokenClick = ::navigateToToken,
          )
        )
      }
      is ChildConfig.Token -> {
        RootComponent.Child.Token(
          tokenComponentFactory(componentContext = componentContext, symbol = config.symbol)
        )
      }
    }

  private fun navigateToToken(symbol: String) {
    navigation.pushNew(ChildConfig.Token(symbol))
  }

  @Serializable
  private sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable data class Token(val symbol: String) : ChildConfig
  }
}
