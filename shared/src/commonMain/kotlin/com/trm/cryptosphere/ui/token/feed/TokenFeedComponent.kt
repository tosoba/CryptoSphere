package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext

interface TokenFeedComponent {
  val symbols: List<String>
  val currentIndex: Int

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      symbols: List<String>,
      currentIndex: Int,
    ): TokenFeedComponent
  }
}
