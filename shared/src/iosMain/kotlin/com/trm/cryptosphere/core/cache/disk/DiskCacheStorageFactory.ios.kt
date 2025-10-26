package com.trm.cryptosphere.core.cache.disk

import com.trm.cryptosphere.core.PlatformContext
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem
import okio.Path.Companion.toPath
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSSearchPathForDirectoriesInDomains
import platform.Foundation.NSUserDomainMask

internal actual fun PlatformContext.createDiskCacheStorage(): CacheStorage =
  DiskCacheStorage(
    fileSystem = FileSystem.SYSTEM,
    directory =
      NSSearchPathForDirectoriesInDomains(
          directory = NSCachesDirectory,
          domainMask = NSUserDomainMask,
          expandTilde = true,
        )
        .first()
        .toString()
        .toPath(),
    maxSize = DiskCacheStorage.DEFAULT_MAX_SIZE,
  )
