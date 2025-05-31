package com.trm.cryptosphere

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.arkivanov.decompose.defaultComponentContext
import com.trm.cryptosphere.ui.root.RootContent
import com.trm.cryptosphere.ui.root.RootDefaultComponent

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    enableEdgeToEdge()
    super.onCreate(savedInstanceState)
    val component = RootDefaultComponent(componentContext = defaultComponentContext())
    setContent { RootContent(component) }
  }
}
