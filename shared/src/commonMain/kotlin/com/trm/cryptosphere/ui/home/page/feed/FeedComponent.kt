package com.trm.cryptosphere.ui.home.page.feed

import com.arkivanov.decompose.ComponentContext

interface FeedComponent {
  val onTokenClick: (String) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenClick: (String) -> Unit,
    ): FeedComponent
  }
}
