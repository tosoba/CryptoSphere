package com.trm.cryptosphere.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable

@Composable
actual fun CryptoSphereTheme(
  darkTheme: Boolean,
  typography: Typography,
  content: @Composable (() -> Unit),
) {
  MaterialTheme(
    colorScheme = if (darkTheme) darkColorScheme else lightColorScheme,
    typography = typography,
    content = content,
  )
}
