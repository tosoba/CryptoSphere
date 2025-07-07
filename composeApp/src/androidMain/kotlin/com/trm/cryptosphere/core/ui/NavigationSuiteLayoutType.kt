package com.trm.cryptosphere.core.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowWidthSizeClass

@Composable
fun navigationSuiteLayoutType(): NavigationSuiteType =
  with(currentWindowAdaptiveInfo()) {
    when {
      windowPosture.isTabletop -> {
        NavigationSuiteType.Companion.NavigationBar
      }
      windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.Companion.EXPANDED ||
        windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.Companion.MEDIUM -> {
        NavigationSuiteType.Companion.NavigationRail
      }
      else -> {
        NavigationSuiteType.Companion.NavigationBar
      }
    }
  }
