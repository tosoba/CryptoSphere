package com.trm.cryptosphere.ui.root

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.materialPredictiveBackAnimatable
import com.trm.cryptosphere.core.ui.StatusBarContentAppearance
import com.trm.cryptosphere.core.ui.StatusBarContentAppearanceEffect
import com.trm.cryptosphere.ui.home.HomeContent
import com.trm.cryptosphere.ui.token.details.TokenDetailsContent
import com.trm.cryptosphere.ui.token.feed.TokenFeedContent

@OptIn(
  ExperimentalMaterial3ExpressiveApi::class,
  ExperimentalSharedTransitionApi::class,
  ExperimentalDecomposeApi::class,
)
@Composable
fun RootContent(component: RootComponent) {
  MaterialExpressiveTheme {
    SharedTransitionLayout {
      ChildStack(
        stack = component.stack,
        modifier = Modifier.fillMaxSize(),
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
        when (val instance = child.instance) {
          is RootComponent.Child.Home -> {
            HomeContent(
              component = instance.component,
              animatedVisibilityScope = this@ChildStack,
              modifier = Modifier.fillMaxSize(),
            )
          }
          is RootComponent.Child.TokenFeed -> {
            StatusBarContentAppearanceEffect(StatusBarContentAppearance.DARK)

            TokenFeedContent(
              component = instance.component,
              animatedVisibilityScope = this@ChildStack,
              modifier = Modifier.fillMaxSize(),
            )
          }
          is RootComponent.Child.TokenDetails -> {
            StatusBarContentAppearanceEffect(StatusBarContentAppearance.DARK)

            TokenDetailsContent(
              component = instance.component,
              animatedVisibilityScope = this@ChildStack,
              modifier = Modifier.fillMaxSize(),
            )
          }
        }
      }
    }
  }
}
