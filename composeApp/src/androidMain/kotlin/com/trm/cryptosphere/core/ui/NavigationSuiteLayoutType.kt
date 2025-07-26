package com.trm.cryptosphere.core.ui

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable

@Composable
fun navigationSuiteLayoutType(): NavigationSuiteType =
  with(currentWindowAdaptiveInfo()) {
    when {
      windowPosture.isTabletop -> NavigationSuiteType.Companion.NavigationBar
      windowSizeClass.isWidthAtLeastBreakpoint(600) -> NavigationSuiteType.Companion.NavigationRail
      else -> NavigationSuiteType.Companion.NavigationBar
    }
  }
