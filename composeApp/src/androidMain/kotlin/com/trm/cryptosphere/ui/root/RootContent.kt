package com.trm.cryptosphere.ui.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.trm.cryptosphere.ui.home.HomeContent
import com.trm.cryptosphere.ui.token.TokenContent

@Composable
fun RootContent(component: RootComponent) {
  MaterialTheme {
    Children(
      stack = component.stack,
      modifier = Modifier.fillMaxSize(),
      animation =
        @OptIn(ExperimentalDecomposeApi::class)
        predictiveBackAnimation(
          backHandler = component.backHandler,
          fallbackAnimation = stackAnimation(fade() + scale()),
          onBack = component::onBackClicked,
        ),
    ) { child ->
      when (val instance = child.instance) {
        is RootComponent.Child.Home -> {
          HomeContent(component = instance.component, modifier = Modifier.fillMaxSize())
        }
        is RootComponent.Child.Token -> {
          TokenContent(component = instance.component, modifier = Modifier.fillMaxSize())
        }
      }
    }
  }
}
