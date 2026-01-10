package com.trm.cryptosphere.ui.root

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner
import com.trm.cryptosphere.ui.home.HomeComponent
import com.trm.cryptosphere.ui.token.feed.TokenFeedComponent
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationComponent

interface RootComponent : BackHandlerOwner {
  val stack: Value<ChildStack<*, Child>>

  fun onBackClicked()

  fun onBackClicked(toIndex: Int)

  sealed interface Child {
    class Home(val component: HomeComponent) : Child

    class TokenNavigation(val component: TokenNavigationComponent) : Child

    class TokenFeed(val component: TokenFeedComponent) : Child
  }
}
