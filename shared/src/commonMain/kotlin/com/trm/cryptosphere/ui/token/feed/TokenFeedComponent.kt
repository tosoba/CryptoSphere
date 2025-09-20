package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.base.StateFlowInstance
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.StateFlow

interface TokenFeedComponent {
  val mainTokenSymbol: StateFlowInstance<String>
  val tokens: StateFlow<List<TokenItem>>
  val tokenCarouselConfig: TokenCarouselConfig
  val navigateToTokenDetails: (String) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mainTokenSymbol: String,
      tokenCarouselConfig: TokenCarouselConfig,
      navigateToTokenDetails: (String) -> Unit,
    ): TokenFeedComponent
  }
}
