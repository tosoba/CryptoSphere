package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.domain.model.TokenItem

interface TokenFeedComponent : ComponentContext {
  val viewModel: TokenFeedViewModel
  val onCurrentFeedTokenChange: (TokenItem?) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      tokenId: Int,
      onCurrentFeedTokenChange: (TokenItem?) -> Unit,
    ): TokenFeedComponent
  }
}
