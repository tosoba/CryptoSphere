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
      append("0.")

      // Count leading zeros after decimal point
      val leadingZeros = -floor(log10(absValue)).toInt() - 1

      // Append leading zeros
      repeat(leadingZeros) { append('0') }

      // Extract significant digits
      val shifted = absValue * 10.0.pow((leadingZeros + significantDecimals).toDouble())
      val significantPart = round(shifted).toLong()
      val significantStr = significantPart.toString().take(significantDecimals)

      append(significantStr)

      // Remove trailing zeros
      while (length > 2 && last() == '0') {
        deleteAt(lastIndex)
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
