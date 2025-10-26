package com.trm.cryptosphere.core.cache.disk

import com.trm.cryptosphere.core.PlatformContext
import io.ktor.client.plugins.cache.storage.CacheStorage

internal expect fun PlatformContext.createDiskCacheStorage(): CacheStorage
