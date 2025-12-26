package com.trm.cryptosphere.ui.root

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.materialPredictiveBackAnimatable
import com.trm.cryptosphere.core.ui.ColorExtractor
import com.trm.cryptosphere.core.ui.DynamicTheme
import com.trm.cryptosphere.core.ui.LocalSharedTransition
import com.trm.cryptosphere.core.ui.SharedTransition
import com.trm.cryptosphere.ui.home.HomeContent
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationContent

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalDecomposeApi::class)
@Composable
fun RootContent(component: RootComponent, colorExtractor: ColorExtractor) {
  var themeImageUrl: String? by remember { mutableStateOf(null) }

  DynamicTheme(imageUrl = themeImageUrl, colorExtractor = colorExtractor) {
    SharedTransitionLayout {
      Column {
        Box(
          modifier =
            Modifier.fillMaxWidth()
              .height(
                with(LocalDensity.current) {
                  WindowInsets.statusBars.getTop(LocalDensity.current).toDp()
                }
              )
              .background(MaterialTheme.colorScheme.surfaceContainer)
        )

        ChildStack(
          stack = component.stack,
          modifier = Modifier.fillMaxWidth().weight(1f),
          animation =
            stackAnimation(
              animator = fade() + scale(),
              predictiveBackParams = {
                PredictiveBackParams(
                  backHandler = component.backHandler,
                  onBack = component::onBackClicked,
                  animatable = ::materialPredictiveBackAnimatable,
                )
              },
            ),
        ) { child ->
          CompositionLocalProvider(
            LocalSharedTransition provides
              SharedTransition(
                sharedTransitionScope = this@SharedTransitionLayout,
                animatedVisibilityScope = this@ChildStack,
              )
          ) {
            when (val instance = child.instance) {
              is RootComponent.Child.Home -> {
                HomeContent(
                  component = instance.component,
                  modifier = Modifier.fillMaxSize(),
                  onImageUrlChange = { themeImageUrl = it },
                )
              }
              is RootComponent.Child.TokenNavigation -> {
                TokenNavigationContent(
                  component = instance.component,
                  modifier = Modifier.fillMaxSize(),
                  onImageUrlChange = { themeImageUrl = it },
                )
              }
            }
          }
        }
      }
    }
  }
}
