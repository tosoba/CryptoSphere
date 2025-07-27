package com.trm.cryptosphere.core.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable

@Composable
fun currentNavigationSuiteType(): NavigationSuiteType =
  with(currentWindowAdaptiveInfo()) {
    when {
      windowPosture.isTabletop -> {
        NavigationSuiteType.NavigationBar
      }
      windowSizeClass.isWidthAtLeastBreakpoint(600) ||
        !windowSizeClass.isHeightAtLeastBreakpoint(480) -> {
        NavigationSuiteType.NavigationRail
      }
      else -> {
        NavigationSuiteType.NavigationBar
      }
    }
  }
