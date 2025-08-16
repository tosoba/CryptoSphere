package com.trm.cryptosphere.core.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier

@OptIn(ExperimentalSharedTransitionApi::class)
@Immutable
data class SharedTransition(
  val sharedTransitionScope: SharedTransitionScope,
  val animatedVisibilityScope: AnimatedVisibilityScope,
)

val LocalSharedTransition =
  staticCompositionLocalOf<SharedTransition> { error("SharedTransition is not provided.") }

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun Modifier.localSharedElement(key: Any): Modifier {
  val sharedTransition = LocalSharedTransition.current
  return with(sharedTransition.sharedTransitionScope) {
    sharedElement(
      sharedContentState = rememberSharedContentState(key),
      animatedVisibilityScope = sharedTransition.animatedVisibilityScope,
    )
  }
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun SharedTransitionPreview(content: @Composable () -> Unit) {
  SharedTransitionLayout {
    AnimatedVisibility(true) {
      CompositionLocalProvider(
        value =
          LocalSharedTransition provides
            SharedTransition(
              sharedTransitionScope = this@SharedTransitionLayout,
              animatedVisibilityScope = this@AnimatedVisibility,
            ),
        content = content,
      )
    }
  }
}
