package com.trm.cryptosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.trm.cryptosphere.ui.root.RootContent

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)

    val app = application as CryptoSphereApplication
    setContent {
      RootContent(
        app.dependencyContainer.rootComponentFactory(
          componentContext = defaultComponentContext(),
          colorExtractor = app.androidDependencyContainer.colorExtractor,
        )
      )
    }
  }
}
