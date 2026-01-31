package com.trm.cryptosphere.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.trm.cryptosphere.shared.MR
import dev.icerock.moko.resources.compose.asFont

@Composable
fun cryptoSphereTypography(): Typography =
  with(Typography()) {
    val displayFontFamily =
      FontFamily(
        listOfNotNull(
          MR.fonts.spacegrotesk_bold.asFont(weight = FontWeight.Bold),
          MR.fonts.spacegrotesk_light.asFont(weight = FontWeight.Light),
          MR.fonts.spacegrotesk_medium.asFont(weight = FontWeight.Medium),
          MR.fonts.spacegrotesk_regular.asFont(weight = FontWeight.Normal),
          MR.fonts.spacegrotesk_semibold.asFont(weight = FontWeight.SemiBold),
        )
      )
    val bodyFontFamily =
      FontFamily(
        listOfNotNull(
          MR.fonts.manrope_bold.asFont(weight = FontWeight.Bold),
          MR.fonts.manrope_extrabold.asFont(weight = FontWeight.ExtraBold),
          MR.fonts.manrope_extralight.asFont(weight = FontWeight.ExtraLight),
          MR.fonts.manrope_light.asFont(weight = FontWeight.Light),
          MR.fonts.manrope_medium.asFont(weight = FontWeight.Medium),
          MR.fonts.manrope_regular.asFont(weight = FontWeight.Normal),
          MR.fonts.manrope_semibold.asFont(weight = FontWeight.SemiBold),
        )
      )
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
