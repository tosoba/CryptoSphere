package com.trm.cryptosphere.core.ui.theme

import androidx.annotation.ArrayRes
import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont

fun cryptoSphereTypography(@ArrayRes certificates: Int): Typography =
  with(Typography()) {
    val provider =
      GoogleFont.Provider(
        providerAuthority = "com.google.android.gms.fonts",
        providerPackage = "com.google.android.gms",
        certificates = certificates,
      )

    val bodyFontFamily =
      FontFamily(Font(googleFont = GoogleFont("Manrope"), fontProvider = provider))
    val displayFontFamily =
      FontFamily(Font(googleFont = GoogleFont("Space Grotesk"), fontProvider = provider))

    Typography(
      displayLarge = displayLarge.copy(fontFamily = displayFontFamily),
      displayMedium = displayMedium.copy(fontFamily = displayFontFamily),
      displaySmall = displaySmall.copy(fontFamily = displayFontFamily),
      headlineLarge = headlineLarge.copy(fontFamily = displayFontFamily),
      headlineMedium = headlineMedium.copy(fontFamily = displayFontFamily),
      headlineSmall = headlineSmall.copy(fontFamily = displayFontFamily),
      titleLarge = titleLarge.copy(fontFamily = displayFontFamily),
      titleMedium = titleMedium.copy(fontFamily = displayFontFamily),
      titleSmall = titleSmall.copy(fontFamily = displayFontFamily),
      bodyLarge = bodyLarge.copy(fontFamily = bodyFontFamily),
      bodyMedium = bodyMedium.copy(fontFamily = bodyFontFamily),
      bodySmall = bodySmall.copy(fontFamily = bodyFontFamily),
      labelLarge = labelLarge.copy(fontFamily = bodyFontFamily),
      labelMedium = labelMedium.copy(fontFamily = bodyFontFamily),
      labelSmall = labelSmall.copy(fontFamily = bodyFontFamily),
    )
  }
