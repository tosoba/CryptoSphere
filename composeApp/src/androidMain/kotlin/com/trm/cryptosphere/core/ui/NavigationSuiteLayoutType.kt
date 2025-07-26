package com.trm.cryptosphere.core.ui

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable

@Composable
fun navigationSuiteLayoutType(): NavigationSuiteType =
  with(currentWindowAdaptiveInfo()) {
    when {
      windowPosture.isTabletop -> {
        NavigationSuiteType.NavigationBar
      }
      windowSizeClass.isWidthAtLeastBreakpoint(600) || isCompactHeight() -> {
        NavigationSuiteType.NavigationRail
      }
      else -> {
        NavigationSuiteType.NavigationBar
      }
    }
  }

fun WindowAdaptiveInfo.isCompactHeight() = !windowSizeClass.isHeightAtLeastBreakpoint(480)
