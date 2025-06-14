package com.trm.cryptosphere.core

actual abstract class PlatformContext private constructor() {
  companion object {
    val INSTANCE = object : PlatformContext() {}
  }
}
