package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.token.TokenComponent

interface RootComponent {
  val stack: Value<ChildStack<*, Child>>

  sealed interface Child {
    class Home(val component: HomeComponent) : Child

    class Token(val tokenComponent: TokenComponent) : Child
  }
}
