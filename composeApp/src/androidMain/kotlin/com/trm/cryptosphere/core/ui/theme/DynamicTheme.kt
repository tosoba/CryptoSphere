package com.trm.cryptosphere.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.graphics.Color
import com.materialkolor.DynamicMaterialTheme
import com.trm.cryptosphere.core.base.cancellableRunCatching
import com.trm.cryptosphere.core.ui.StatusBarContentAppearance
import com.trm.cryptosphere.core.ui.StatusBarContentAppearanceEffect

@Composable
fun DynamicTheme(
  imageUrl: String?,
  colorExtractor: ColorExtractor,
  fallback: Color = MaterialTheme.colorScheme.primary,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  StatusBarContentAppearanceEffect(
    if (useDarkTheme) StatusBarContentAppearance.LIGHT else StatusBarContentAppearance.DARK
  )

  val result by
    produceState<ColorExtractor.Result?>(initialValue = null, imageUrl, colorExtractor) {
      value =
        cancellableRunCatching { imageUrl?.let { colorExtractor.calculatePrimaryColor(it) } }
          .getOrNull()
    }
  DynamicMaterialTheme(
    seedColor = result?.color ?: fallback,
    primary = if (result == null) MaterialTheme.colorScheme.primary else null,
    secondary = if (result == null) MaterialTheme.colorScheme.secondary else null,
    tertiary = if (result == null) MaterialTheme.colorScheme.tertiary else null,
    error = if (result == null) MaterialTheme.colorScheme.error else null,
    isDark = useDarkTheme,
    animate = result.let { it != null && !it.cached },
    content = content,
  )
}
