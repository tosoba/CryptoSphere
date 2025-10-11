package com.trm.cryptosphere.core.initializer

import android.content.Context
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.startup.Initializer
import com.trm.cryptosphere.CryptoSphereApplication
import kotlinx.coroutines.launch

class PeriodicTokenSyncInitializer : Initializer<Unit> {
  override fun create(context: Context) {
    ProcessLifecycleOwner.get().lifecycleScope.launch {
      (context as CryptoSphereApplication)
        .dependencyContainer
        .enqueuePeriodicTokenSyncUseCase
        .value()
    }
  }

  override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}
