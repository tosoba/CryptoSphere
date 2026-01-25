package com.trm.cryptosphere.core.cache.util

import com.trm.cryptosphere.core.PlatformContext
import okio.Path
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

actual val PlatformContext.cachePath: Path
  get() =
    NSSearchPathForDirectoriesInDomains(
        directory = NSCachesDirectory,
        domainMask = NSUserDomainMask,
        expandTilde = true,
      )
      .first()
      .toString()
      .toPath()
