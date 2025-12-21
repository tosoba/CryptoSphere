package com.trm.cryptosphere.core.util

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass

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
  windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND) &&
    !windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)

@Composable
fun WindowAdaptiveInfo.isExpandedHeight(): Boolean =
  !windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_EXPANDED_LOWER_BOUND)

@Composable
fun WindowAdaptiveInfo.isCompactHeight(): Boolean =
  !windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
