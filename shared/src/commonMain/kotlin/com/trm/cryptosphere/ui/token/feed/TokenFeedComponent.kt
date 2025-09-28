package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingData
import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.base.StateFlowInstance
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import kotlinx.coroutines.flow.Flow

interface TokenFeedComponent {
  val mainTokenId: StateFlowInstance<Int>
  val tokens: Flow<PagingData<TokenItem>>
  val tokenCarouselConfig: TokenCarouselConfig
  val navigateToTokenDetails: (Int) -> Unit

  fun interface Factory {
    operator fun invoke(
      componentContext: ComponentContext,
      mainTokenId: Int,
      tokenCarouselConfig: TokenCarouselConfig,
      navigateToTokenDetails: (Int) -> Unit,
    ): TokenFeedComponent
  }
}
