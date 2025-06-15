package com.trm.cryptosphere

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.trm.cryptosphere.di.DependencyContainer
import com.trm.cryptosphere.ui.root.RootContent

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    val component = container.rootComponent(defaultComponentContext())
    setContent { RootContent(component) }
  }
}

private val Activity.container: DependencyContainer
  get() = (application as CryptoSphereApplication).dependencyContainer
