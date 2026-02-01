package com.trm.cryptosphere.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.materialkolor.DynamicMaterialTheme

@Composable
expect fun CryptoSphereTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  typography: Typography = cryptoSphereTypography(),
  content: @Composable () -> Unit,
)

@Composable
fun CryptoSphereDynamicTheme(
  colorExtractorResult: ColorExtractor.Result?,
  fallback: Color = MaterialTheme.colorScheme.primary,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  typography: Typography = cryptoSphereTypography(),
  content: @Composable () -> Unit,
) {
  CryptoSphereTheme(darkTheme = useDarkTheme, typography = typography) {
    DynamicMaterialTheme(
      seedColor = colorExtractorResult?.color ?: fallback,
      primary = if (colorExtractorResult == null) MaterialTheme.colorScheme.primary else null,
      secondary = if (colorExtractorResult == null) MaterialTheme.colorScheme.secondary else null,
      tertiary = if (colorExtractorResult == null) MaterialTheme.colorScheme.tertiary else null,
      error = if (colorExtractorResult == null) MaterialTheme.colorScheme.error else null,
      isDark = useDarkTheme,
      animate = colorExtractorResult.let { it != null && !it.cached },
      typography = typography,
      content = content,
    )
  }
}

@Composable
fun CryptoSphereDynamicTheme(
  colorExtractorResultProvider: ColorExtractorResultProvider,
  fallback: Color = MaterialTheme.colorScheme.primary,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  CryptoSphereDynamicTheme(
    colorExtractorResult =
      colorExtractorResultProvider.colorExtractorResult.collectAsStateWithLifecycle().value,
    fallback = fallback,
    useDarkTheme = useDarkTheme,
    content = content,
  )
}
