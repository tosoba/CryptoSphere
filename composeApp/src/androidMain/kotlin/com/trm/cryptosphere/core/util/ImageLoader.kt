package com.trm.cryptosphere.core.util

import android.content.Context
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.intercept.Interceptor
import coil3.memory.MemoryCache
import okio.Path.Companion.toPath

fun imageLoader(context: Context, interceptors: Set<Interceptor> = emptySet()): ImageLoader =
  ImageLoader.Builder(context)
    .components { interceptors.forEach(::add) }
    .memoryCache { MemoryCache.Builder().maxSizePercent(context = context, percent = 0.25).build() }
    .diskCache {
      DiskCache.Builder()
        .directory(context.cacheDir.absolutePath.toPath().resolve("coil_cache"))
        .build()
    }
    .build()
