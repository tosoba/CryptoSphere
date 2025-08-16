package com.trm.cryptosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.trm.cryptosphere.core.ui.ColorExtractor
import com.trm.cryptosphere.core.ui.imageLoader
import com.trm.cryptosphere.core.util.container
import com.trm.cryptosphere.ui.root.RootContent

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    val component = container.createRootComponent(defaultComponentContext())
    val colorExtractor = ColorExtractor(imageLoader(this), this)
    setContent { RootContent(component, colorExtractor) }
  }
}
