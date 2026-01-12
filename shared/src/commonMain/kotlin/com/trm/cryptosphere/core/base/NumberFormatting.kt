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

fun Double.fullDecimalFormat(significantDecimals: Int = 3, signed: Boolean = false): String =
  buildString {
    val absValue = abs(this@fullDecimalFormat)

    // Handle Sign
    if (signed) {
      append(if (this@fullDecimalFormat.sign >= 0.0) "+" else "-")
    } else if (this@fullDecimalFormat < 0) {
      append("-")
    }

    if (absValue >= 1.0) {
      append(absValue.formatNoScientific(significantDecimals))
    } else if (absValue == 0.0) {
      append("0")
    } else {
      // 1. Calculate leading zeros based on raw input
      var leadingZeros = -floor(log10(absValue)).toInt() - 1

      // 2. Calculate the multiplier required to get significant digits
      val power = leadingZeros + significantDecimals
      val shifted = absValue * 10.0.pow(power.toDouble())

      // 3. Round to integer
      var significantPart = round(shifted).toLong()

      // 4. Check for rounding overflow (e.g., 0.999 -> 1000)
      // If the rounded number has more digits than requested, we bumped a magnitude.
      if (significantPart.toString().length > significantDecimals) {
        significantPart /= 10
        leadingZeros--
      }

      // 5. Construct the string
      if (leadingZeros < 0) {
        // If leading zeros dropped below 0, the number rounded up to >= 1.0
        append("1")
      } else {
        append("0.")
        repeat(leadingZeros) { append('0') }
        append(significantPart)
      }

      // 6. Remove trailing zeros
      // Check length > 2 to protect "0."
      if (contains('.')) {
        while (length > 2 && last() == '0') {
          deleteAt(lastIndex)
        }
        // Optional: Remove trailing decimal point if it occurs (e.g. "1.")
        if (last() == '.') {
          deleteAt(lastIndex)
        }
      }
    }
  }

/** Formats a Double without scientific notation, avoiding toString() issues */
private fun Double.formatNoScientific(decimals: Int): String {
  if (this.isNaN()) return "NaN"
  if (this.isInfinite()) return if (this > 0) "Infinity" else "-Infinity"
  if (this == 0.0) return "0"

  val absValue = abs(this)
  val multiplier = 10.0.pow(decimals.toDouble())
  val rounded = round(absValue * multiplier) / multiplier

  // Split into integer and fractional parts
  val integerPart = floor(rounded).toLong()
  val fractionalPart = rounded - integerPart

  return buildString {
    if (this@formatNoScientific < 0) append('-')
    append(integerPart)

    if (decimals > 0 && fractionalPart > 0) {
      append('.')

      // Extract decimal digits manually
      var fraction = fractionalPart
      repeat(decimals) {
        fraction *= 10
        val digit = floor(fraction).toInt()
        append(digit)
        fraction -= digit
      }

      // Remove trailing zeros
      while (last() == '0') {
        deleteAt(lastIndex)
      }

      // Remove decimal point if no decimals left
      if (last() == '.') {
        deleteAt(lastIndex)
      }
    }
  }
}
