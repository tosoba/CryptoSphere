package com.trm.cryptosphere.core.cache.util

import com.trm.cryptosphere.core.PlatformContext
import okio.Path
import okio.Path.Companion.toOkioPath

actual val PlatformContext.cachePath: Path
  get() = cacheDir.toOkioPath()
