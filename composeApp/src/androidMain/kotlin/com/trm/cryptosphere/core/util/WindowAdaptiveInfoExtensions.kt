package com.trm.cryptosphere.core.util

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable

@Composable
fun WindowAdaptiveInfo.toNavigationSuiteType(): NavigationSuiteType =
  when {
    windowPosture.isTabletop -> {
      NavigationSuiteType.NavigationBar
    }
    isAtLeastMediumWidthAndAtMostMediumHeight() || isCompactHeight() -> {
      NavigationSuiteType.NavigationRail
    }
    else -> {
      NavigationSuiteType.NavigationBar
    }
  }

@Composable
fun WindowAdaptiveInfo.isAtLeastMediumWidthAndAtMostMediumHeight(): Boolean =
  windowSizeClass.isWidthAtLeastBreakpoint(600) && !windowSizeClass.isHeightAtLeastBreakpoint(900)

@Composable
fun WindowAdaptiveInfo.isCompactHeight(): Boolean = !windowSizeClass.isHeightAtLeastBreakpoint(480)
