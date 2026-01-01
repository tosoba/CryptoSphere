package com.trm.cryptosphere.ui.root

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.domain.repository.TokenHistoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class RootViewModel(
  private val tokenHistoryRepository: TokenHistoryRepository,
  dispatchers: AppCoroutineDispatchers,
) : InstanceKeeper.Instance {
  private val scope = CoroutineScope(dispatchers.main + SupervisorJob())

  fun onTokenSelected(tokenId: Int) {
    scope.launch { tokenHistoryRepository.addTokenToHistory(tokenId) }
  }

  override fun onDestroy() {
    scope.cancel()
  }
}
