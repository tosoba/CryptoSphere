package com.trm.cryptosphere.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.trm.cryptosphere.core.ui.StatusBarContentAppearance
import com.trm.cryptosphere.core.ui.StatusBarContentAppearanceEffect

@Composable
fun DynamicTheme(
  colorExtractorResult: ColorExtractor.Result?,
  fallback: Color = MaterialTheme.colorScheme.primary,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  StatusBarContentAppearanceEffect(
    if (useDarkTheme) StatusBarContentAppearance.LIGHT else StatusBarContentAppearance.DARK
  )

  DynamicMaterialTheme(
    seedColor = colorExtractorResult?.color ?: fallback,
    primary = if (colorExtractorResult == null) MaterialTheme.colorScheme.primary else null,
    secondary = if (colorExtractorResult == null) MaterialTheme.colorScheme.secondary else null,
    tertiary = if (colorExtractorResult == null) MaterialTheme.colorScheme.tertiary else null,
    error = if (colorExtractorResult == null) MaterialTheme.colorScheme.error else null,
    isDark = useDarkTheme,
    animate = colorExtractorResult.let { it != null && !it.cached },
    content = content,
  )
}
