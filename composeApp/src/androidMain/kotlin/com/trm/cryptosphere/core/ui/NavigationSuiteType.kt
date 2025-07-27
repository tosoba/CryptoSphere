package com.trm.cryptosphere.core.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
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
      isAtLeastMediumWidthAndAtMostMediumHeight() || isCompactHeight() -> {
        NavigationSuiteType.NavigationRail
      }
      else -> {
        NavigationSuiteType.NavigationBar
      }
    }
  }

@Composable
private fun WindowAdaptiveInfo.isAtLeastMediumWidthAndAtMostMediumHeight(): Boolean =
  windowSizeClass.isWidthAtLeastBreakpoint(600) && !windowSizeClass.isHeightAtLeastBreakpoint(900)

@Composable
private fun WindowAdaptiveInfo.isCompactHeight(): Boolean =
  !windowSizeClass.isHeightAtLeastBreakpoint(480)
