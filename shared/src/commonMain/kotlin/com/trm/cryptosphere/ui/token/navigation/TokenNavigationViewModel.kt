package com.trm.cryptosphere.ui.token.navigation

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import com.trm.cryptosphere.domain.repository.TokenRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class TokenNavigationViewModel(
  initialTokenIds: List<Int>,
  private val tokenRepository: TokenRepository,
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  private val navigationTokensIds: MutableStateFlow<List<Int>> = MutableStateFlow(initialTokenIds)
  val currentNavigationTokenId: Flow<Int?> = navigationTokensIds.map { it.lastOrNull() }
  val navigationTokens: Flow<List<TokenItem>> =
    navigationTokensIds.map {
      val tokens = tokenRepository.getTokensByIds(it).associateBy(TokenItem::id)
      it.mapNotNull { tokenId -> tokens[tokenId] }
    }

  fun updateNavigationTokenIds(tokenIds: List<Int>) {
    navigationTokensIds.value = tokenIds
  }

  fun addTokenToHistory(tokenId: Int) {
    scope.launch { tokenHistoryRepository.addTokenToHistory(tokenId) }
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
