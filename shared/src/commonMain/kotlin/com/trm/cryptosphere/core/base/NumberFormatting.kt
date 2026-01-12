package com.trm.cryptosphere.core.base

import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.log10
import kotlin.math.pow
import kotlin.math.round
import kotlin.math.sign

fun Double.shortDecimalFormat(): String {
  val trillion = 1_000_000_000_000.0
  val billion = 1_000_000_000.0
  val million = 1_000_000.0
  val thousand = 1_000.0
  return when {
    this >= trillion -> "${(this / trillion).formatNoScientific(1)}T"
    this >= billion -> "${(this / billion).formatNoScientific(1)}B"
    this >= million -> "${(this / million).formatNoScientific(1)}M"
    this >= thousand -> "${(this / thousand).formatNoScientific(1)}K"
    else -> formatNoScientific(1)
  }
}

fun Double.fullDecimalFormat(significantDecimals: Int = 3, signed: Boolean = false): String {
  // 1. Handle non-finite numbers first
  if (!this.isFinite()) {
    return when {
      this.isNaN() -> "NaN"
      this > 0 -> if (signed) "+Infinity" else "Infinity"
      else -> "-Infinity"
    }
  }

  val absValue = abs(this@fullDecimalFormat)

  // 2. Handle Zero
  if (absValue == 0.0) {
    return if (signed) "+0" else "0"
  }

  return buildString {
      // 3. Handle Sign
      if (signed) {
        append(if (this@fullDecimalFormat.sign >= 0.0) "+" else "-")
      } else if (this@fullDecimalFormat < 0) {
        append("-")
      }

      if (absValue >= 1.0) {
        // Treat significantDecimals as fixed decimal places for numbers >= 1
        append(absValue.formatNoScientific(significantDecimals))
      } else {
        // Significant digits logic for numbers < 1
        val leadingZeros = -floor(log10(absValue)).toInt() - 1
        val totalDecimalsNeeded = leadingZeros + significantDecimals

        // Use the robust scaling logic
        val formatted = absValue.formatNoScientific(totalDecimalsNeeded)

        // If rounding caused the number to become 1.0 (e.g., 0.999 -> 1)
        if (!formatted.startsWith("0.")) {
          append(formatted)
        } else {
          append(formatted)
        }
      }
    }
    .removeExtraZeros()
}

private fun Double.formatNoScientific(decimals: Int): String {
  if (!this.isFinite()) return this.toString()

  val absValue = abs(this)
  val multiplier = 10.0.pow(decimals.toDouble())
  val scaled = round(absValue * multiplier).toLong()
  val scaledStr = scaled.toString()

  return if (decimals <= 0) {
    scaledStr
  } else {
    val padded = scaledStr.padStart(decimals + 1, '0')
    val splitAt = padded.length - decimals
    val intPart = padded.take(splitAt)
    val fracPart = padded.substring(splitAt)
    "$intPart.$fracPart".removeExtraZeros()
  }
}

private fun String.removeExtraZeros(): String {
  if (!contains('.')) return this
  val trimmed = dropLastWhile { it == '0' }
  return if (trimmed.endsWith('.')) trimmed.dropLast(1) else trimmed
}
