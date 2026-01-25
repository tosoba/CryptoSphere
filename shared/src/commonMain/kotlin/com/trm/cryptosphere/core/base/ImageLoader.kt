package com.trm.cryptosphere.core.base

import coil3.ImageLoader
import coil3.PlatformContext
import coil3.disk.DiskCache
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import okio.Path

fun imageLoader(
  context: PlatformContext,
  cacheDir: Path,
  interceptors: Set<Interceptor> = emptySet(),
): ImageLoader =
  ImageLoader.Builder(context)
    .components { interceptors.forEach(::add) }
    .memoryCache { MemoryCache.Builder().maxSizePercent(context = context, percent = 0.25).build() }
    .diskCache { DiskCache.Builder().directory(cacheDir.resolve("coil_cache")).build() }
    .build()
