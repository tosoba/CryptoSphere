package com.trm.cryptosphere.core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import com.materialkolor.PaletteStyle
import com.materialkolor.ktx.toDynamicScheme
import com.materialkolor.toColorScheme
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.value
import platform.CoreGraphics.CGFloatVar
import platform.UIKit.UIColor

@OptIn(ExperimentalForeignApi::class)
fun UIColor.toComposeColor(): Color = memScoped {
  val red = alloc<CGFloatVar>()
  val green = alloc<CGFloatVar>()
  val blue = alloc<CGFloatVar>()
  val alpha = alloc<CGFloatVar>()

  getRed(red = red.ptr, green = green.ptr, blue = blue.ptr, alpha = alpha.ptr)

  Color(
    red = red.value.toFloat(),
    green = green.value.toFloat(),
    blue = blue.value.toFloat(),
    alpha = alpha.value.toFloat(),
  )
}

@Suppress("unused") // Used in Swift
fun Color.toUIColor(): UIColor =
  UIColor(
    red = red.toDouble(),
    green = green.toDouble(),
    blue = blue.toDouble(),
    alpha = alpha.toDouble(),
  )

@Suppress("unused") // Used in Swift
fun Color.toColorScheme(isDark: Boolean): ColorScheme =
  toDynamicScheme(isDark = isDark, style = PaletteStyle.TonalSpot).toColorScheme()
