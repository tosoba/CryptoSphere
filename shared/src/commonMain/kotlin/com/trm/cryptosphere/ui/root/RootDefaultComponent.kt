package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.popTo
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.ui.home.HomeDefaultComponent
import com.trm.cryptosphere.ui.token.TokenDefaultComponent
import kotlinx.serialization.Serializable

class RootDefaultComponent(componentContext: ComponentContext) :
  RootComponent, ComponentContext by componentContext {
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
      ChildConfig.Home -> RootComponent.Child.Home(HomeDefaultComponent(componentContext))
      is ChildConfig.Token -> RootComponent.Child.Token(TokenDefaultComponent(componentContext))
    }

  @Serializable
  private sealed interface ChildConfig {
    @Serializable data object Home : ChildConfig

    @Serializable data object Token : ChildConfig
  }

  class Factory(
    // TODO: child component factories as arguments
    // TODO: dependencies from DependencyContainer as arguments (factory should be DI-independent)
  ) : RootComponent.Factory {
    override fun create(componentContext: ComponentContext): RootComponent =
      // TODO: pass dependencies from DependencyContainer provided via Factory constructor
      RootDefaultComponent(componentContext)
  }
}
