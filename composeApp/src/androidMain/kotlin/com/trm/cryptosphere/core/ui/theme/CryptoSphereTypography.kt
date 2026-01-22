package com.trm.cryptosphere.core.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.trm.cryptosphere.R

private val provider =
  GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs,
  )

private val robotoSerif = GoogleFont("Roboto Serif")
private val roboto = GoogleFont("Roboto")

private val baseline = Typography()

val CryptoSphereTypography =
  Typography(
    displayLarge =
      baseline.displayLarge.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.displayLarge.fontWeight!!,
            )
          )
      ),
    displayMedium =
      baseline.displayMedium.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.displayMedium.fontWeight!!,
            )
          )
      ),
    displaySmall =
      baseline.displaySmall.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.displaySmall.fontWeight!!,
            )
          )
      ),
    headlineLarge =
      baseline.headlineLarge.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.headlineLarge.fontWeight!!,
            )
          )
      ),
    headlineMedium =
      baseline.headlineMedium.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.headlineMedium.fontWeight!!,
            )
          )
      ),
    headlineSmall =
      baseline.headlineSmall.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.headlineSmall.fontWeight!!,
            )
          )
      ),
    titleLarge =
      baseline.titleLarge.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.titleLarge.fontWeight!!,
            )
          )
      ),
    titleMedium =
      baseline.titleMedium.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.titleMedium.fontWeight!!,
            )
          )
      ),
    titleSmall =
      baseline.titleSmall.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = robotoSerif,
              fontProvider = provider,
              weight = baseline.titleSmall.fontWeight!!,
            )
          )
      ),
    bodyLarge =
      baseline.bodyLarge.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = roboto,
              fontProvider = provider,
              weight = baseline.bodyLarge.fontWeight!!,
            )
          )
      ),
    bodyMedium =
      baseline.bodyMedium.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = roboto,
              fontProvider = provider,
              weight = baseline.bodyMedium.fontWeight!!,
            )
          )
      ),
    bodySmall =
      baseline.bodySmall.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = roboto,
              fontProvider = provider,
              weight = baseline.bodySmall.fontWeight!!,
            )
          )
      ),
    labelLarge =
      baseline.labelLarge.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = roboto,
              fontProvider = provider,
              weight = baseline.labelLarge.fontWeight!!,
            )
          )
      ),
    labelMedium =
      baseline.labelMedium.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = roboto,
              fontProvider = provider,
              weight = baseline.labelMedium.fontWeight!!,
            )
          )
      ),
    labelSmall =
      baseline.labelSmall.copy(
        fontFamily =
          FontFamily(
            Font(
              googleFont = roboto,
              fontProvider = provider,
              weight = baseline.labelSmall.fontWeight!!,
            )
          )
      ),
  )
