package com.trm.cryptosphere.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.materialkolor.DynamicMaterialTheme

@Composable
fun CryptoSphereTheme(
  colorExtractorResult: ColorExtractor.Result?,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  typography: Typography = cryptoSphereTypography(),
  content: @Composable () -> Unit,
) {
  DynamicMaterialTheme(
    seedColor = colorExtractorResult?.color ?: fallbackSeedColor,
    isDark = useDarkTheme,
    animate = colorExtractorResult.let { it != null && !it.cached },
    typography = typography,
    content = content,
  )
}

@Composable
fun CryptoSphereTheme(
  colorExtractorResultProvider: ColorExtractorResultProvider,
  useDarkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  CryptoSphereTheme(
    colorExtractorResult =
      colorExtractorResultProvider.colorExtractorResult.collectAsStateWithLifecycle().value,
    useDarkTheme = useDarkTheme,
    content = content,
  )
}

val fallbackSeedColor = Color(0xFF324463)
