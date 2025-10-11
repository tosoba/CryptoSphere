package com.trm.cryptosphere.domain.usecase

import com.trm.cryptosphere.domain.manager.BackgroundJobsManager
import com.trm.cryptosphere.domain.repository.TokenRepository

class PerformInitialTokensSyncUseCase(
  private val tokenRepository: TokenRepository,
  private val backgroundJobsManager: BackgroundJobsManager,
) {
  suspend operator fun invoke() {
    if (tokenRepository.getTokensCount() == 0) {
      tokenRepository.performFullTokensSync()
      backgroundJobsManager.enqueuePeriodicTokenSync()
    }
  }
}
