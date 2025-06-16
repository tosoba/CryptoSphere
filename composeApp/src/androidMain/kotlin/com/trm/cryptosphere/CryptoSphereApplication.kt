package com.trm.cryptosphere

import android.app.Application
import com.trm.cryptosphere.di.DependencyContainer
import com.trm.cryptosphere.di.DependencyContainerHolder

class CryptoSphereApplication : Application(), DependencyContainerHolder {
  override val dependencyContainer by lazy { DependencyContainer(this) }

  override fun onCreate() {
    super.onCreate()
  }
}
