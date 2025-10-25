package com.trm.cryptosphere.ui.token.feed

import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  mainTokenId: Int,
  override val tokenCarouselConfig: TokenCarouselConfig,
  override val navigateBack: () -> Unit,
  override val navigateHome: () -> Unit,
  private val navigateToTokenFeed: (Int, TokenCarouselConfig) -> Unit,
  override val navigateToTokenDetails: (Int) -> Unit,
  tokenRepository: TokenRepository,
  dispatchers: AppCoroutineDispatchers,
) : TokenFeedComponent, ComponentContext by componentContext {
  private val scope = coroutineScope(dispatchers.main + SupervisorJob())

  override val tokens: Flow<PagingData<TokenItem>> =
    tokenRepository.getTokensBySharedTags(mainTokenId).cachedIn(scope)

  override fun navigateToTokenFeed(mainTokenId: Int) {
    navigateToTokenFeed(mainTokenId, tokenCarouselConfig)
  }
}
