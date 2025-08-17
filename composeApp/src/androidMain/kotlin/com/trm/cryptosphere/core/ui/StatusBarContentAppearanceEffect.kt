package com.trm.cryptosphere.core.ui

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.view.WindowInsetsControllerCompat

@Composable
fun StatusBarContentAppearanceEffect(appearance: StatusBarContentAppearance) {
  LocalActivity.current?.window?.let { window ->
    LaunchedEffect(appearance) {
      WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars =
        appearance != StatusBarContentAppearance.LIGHT
    }
  }
}

enum class StatusBarContentAppearance {
  DARK,
  LIGHT,
}
