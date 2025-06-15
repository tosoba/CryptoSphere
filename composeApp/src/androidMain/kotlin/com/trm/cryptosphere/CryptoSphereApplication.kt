package com.trm.cryptosphere

import android.app.Application
import com.trm.cryptosphere.data.di.DataModule
import com.trm.cryptosphere.di.AppModule

class CryptoSphereApplication : Application() {
  val appModule by lazy { AppModule(dataModule = DataModule.create(this)) }

  override fun onCreate() {
    super.onCreate()
  }
}
