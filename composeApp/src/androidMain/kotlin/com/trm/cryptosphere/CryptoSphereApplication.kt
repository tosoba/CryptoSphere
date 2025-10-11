package com.trm.cryptosphere

import android.app.Application
import com.trm.cryptosphere.di.AndroidDependencyContainer
import com.trm.cryptosphere.di.DependencyContainer
import com.trm.cryptosphere.domain.manager.AndroidBackgroundJobsManager

class CryptoSphereApplication : Application() {
  val dependencyContainer by lazy { DependencyContainer(this, AndroidBackgroundJobsManager()) }
  val androidDependencyContainer by lazy { AndroidDependencyContainer(this) }
}
