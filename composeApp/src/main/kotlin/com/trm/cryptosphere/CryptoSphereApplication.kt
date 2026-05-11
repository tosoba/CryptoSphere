package com.trm.cryptosphere

import android.app.Application
import com.trm.cryptosphere.di.AndroidDependencyContainer
import com.trm.cryptosphere.di.DependencyContainer
import com.trm.cryptosphere.manager.AndroidBackgroundJobsManager

class CryptoSphereApplication : Application() {
  val dependencyContainer by lazy { DependencyContainer(this, AndroidBackgroundJobsManager(this)) }
  val androidDependencyContainer by lazy { AndroidDependencyContainer(this) }
}
