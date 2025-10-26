package com.trm.cryptosphere.core.cache.disk

import com.trm.cryptosphere.core.PlatformContext
import io.ktor.client.plugins.cache.storage.CacheStorage
import okio.FileSystem
import okio.Path.Companion.toOkioPath

internal actual fun PlatformContext.createDiskCacheStorage(): CacheStorage =
  DiskCacheStorage(
    fileSystem = FileSystem.SYSTEM,
    directory = cacheDir.toOkioPath(),
    maxSize = DiskCacheStorage.DEFAULT_MAX_SIZE,
  )
