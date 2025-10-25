package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.Flow

interface TokenFeedComponent {
  val tokens: Flow<PagingData<TokenItem>>
  val tokenCarouselConfig: TokenCarouselConfig
  val navigateToTokenDetails: (Int) -> Unit

  fun navigateToTokenFeed(mainTokenId: Int)

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mainTokenId: Int,
      tokenCarouselConfig: TokenCarouselConfig,
      navigateToTokenFeed: (Int, TokenCarouselConfig) -> Unit,
      navigateToTokenDetails: (Int) -> Unit,
    ): TokenFeedComponent
  }
}
