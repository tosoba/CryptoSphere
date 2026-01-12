package com.trm.cryptosphere.core.base

import kotlin.test.Test
import kotlin.test.assertEquals

class DecimalFormatTest {
  @Test
  fun `rounding 0_9997 up to 1_0 (overflow to integer)`() {
    val input = 0.9997435
    val expected = "1"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `rounding 0_09997 up to 0_1 (overflow to next magnitude)`() {
    val input = 0.09997435
    val expected = "0.1"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `rounding 0_009997 up to 0_01`() {
    val input = 0.009997435
    val expected = "0.01"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `standard rounding down`() {
    val input = 0.12345
    val expected = "0.123"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `standard rounding up`() {
    val input = 0.12365
    val expected = "0.124"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `simple decimal exact match`() {
    val input = 0.5
    val expected = "0.5"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `small number with leading zeros`() {
    val input = 0.001234
    val expected = "0.00123"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `very small number`() {
    val input = 0.0000056789
    val expected = "0.000005679"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 4))
  }

  @Test
  fun `removes trailing zeros`() {
    val input = 0.50001
    val expected = "0.5"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `removes trailing zeros on magnitude jump`() {
    val input = 0.10001
    val expected = "0.1"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `large number exact integer`() {
    val input = 100.0
    val expected = "100"
    assertEquals(expected, input.fullDecimalFormat())
  }

  @Test
  fun `large number with rounding`() {
    val input = 1234.5678
    val expected = "1234.568"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `zero is formatted correctly`() {
    val input = 0.0
    val expected = "0"
    assertEquals(expected, input.fullDecimalFormat())
  }

  @Test
  fun `negative zero is normalized`() {
    val input = -0.0
    val expected = "0"
    assertEquals(expected, input.fullDecimalFormat())
  }

  @Test
  fun `negative rounding overflow`() {
    val input = -0.9997
    val expected = "-1"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `negative standard decimal`() {
    val input = -0.1234
    val expected = "-0.123"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3))
  }

  @Test
  fun `signed flag adds plus to positive`() {
    val input = 0.123
    val expected = "+0.123"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3, signed = true))
  }

  @Test
  fun `signed flag keeps minus on negative`() {
    val input = -0.123
    val expected = "-0.123"
    assertEquals(expected, input.fullDecimalFormat(significantDecimals = 3, signed = true))
  }

  @Test
  fun `signed flag adds plus to zero`() {
    val input = 0.0
    val expected = "+0"
    assertEquals(expected, input.fullDecimalFormat(signed = true))
  }

  @Test
  fun `handles NaN`() {
    val input = Double.NaN
    val expected = "NaN"
    assertEquals(expected, input.fullDecimalFormat())
  }

  @Test
  fun `handles Infinity`() {
    val input = Double.POSITIVE_INFINITY
    val expected = "Infinity"
    assertEquals(expected, input.fullDecimalFormat())
  }
}
