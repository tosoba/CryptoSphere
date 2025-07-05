package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext

interface NewsFeedComponent {
  val onTokenClick: (String) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      onTokenClick: (String) -> Unit,
    ): NewsFeedComponent
  }
}
