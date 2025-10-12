package com.trm.cryptosphere.domain.usecase

import com.trm.cryptosphere.di.DependencyContainer

@Suppress("unused") // Used in Swift
suspend fun enqueuePeriodicTokensSyncUseCase(container: DependencyContainer) {
  container.enqueuePeriodicTokensSyncUseCase.value()
}
