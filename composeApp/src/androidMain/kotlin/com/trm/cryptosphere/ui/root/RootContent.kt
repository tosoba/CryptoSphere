package com.trm.cryptosphere.ui.root

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.experimental.stack.ChildStack
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.PredictiveBackParams
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.experimental.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.materialPredictiveBackAnimatable
import com.trm.cryptosphere.R
import com.trm.cryptosphere.core.ui.LocalSharedTransition
import com.trm.cryptosphere.core.ui.SharedTransition
import com.trm.cryptosphere.core.ui.StatusBarContentAppearance
import com.trm.cryptosphere.core.ui.StatusBarContentAppearanceEffect
import com.trm.cryptosphere.core.ui.theme.CryptoSphereDynamicTheme
import com.trm.cryptosphere.core.ui.theme.cryptoSphereTypography
import com.trm.cryptosphere.ui.home.HomeContent
import com.trm.cryptosphere.ui.token.navigation.TokenNavigationContent

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalDecomposeApi::class)
@Composable
fun RootContent(component: RootComponent) {
  CryptoSphereDynamicTheme(
    colorExtractorResult = component.colorExtractorResult.collectAsStateWithLifecycle().value,
    typography = cryptoSphereTypography(R.array.com_google_android_gms_fonts_certs),
  ) {
    StatusBarContentAppearanceEffect(
      if (isSystemInDarkTheme()) StatusBarContentAppearance.LIGHT
      else StatusBarContentAppearance.DARK
    )

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
        CompositionLocalProvider(
          LocalSharedTransition provides
            SharedTransition(
              sharedTransitionScope = this@SharedTransitionLayout,
              animatedVisibilityScope = this@ChildStack,
            )
        ) {
          when (val instance = child.instance) {
            is RootComponent.Child.Home -> {
              HomeContent(component = instance.component)
            }
            is RootComponent.Child.TokenNavigation -> {
              TokenNavigationContent(component = instance.component)
            }
            is RootComponent.Child.TokenFeed -> {
              // used only on iOS
            }
          }
        }
      }
    }
  }
}
