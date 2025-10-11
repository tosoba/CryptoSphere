package com.trm.cryptosphere.domain.manager

interface BackgroundJobsManager {
  fun enqueuePeriodicTokensSync()
}
