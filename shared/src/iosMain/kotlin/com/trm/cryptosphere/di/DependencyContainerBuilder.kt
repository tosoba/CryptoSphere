package com.trm.cryptosphere.di

import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.domain.manager.BackgroundJobsManager

@Suppress("unused") // Used in Swift
fun buildDependencyContainer(backgroundJobsManager: BackgroundJobsManager): DependencyContainer =
  DependencyContainer(
    context = PlatformContext.INSTANCE,
    backgroundJobsManager = backgroundJobsManager,
  )
