package com.trm.cryptosphere

import android.app.Application
import com.trm.cryptosphere.di.DependencyContainer

class CryptoSphereApplication : Application() {
  val dependencyContainer by lazy { DependencyContainer(this) }

  override fun onCreate() {
    super.onCreate()
  }
}
