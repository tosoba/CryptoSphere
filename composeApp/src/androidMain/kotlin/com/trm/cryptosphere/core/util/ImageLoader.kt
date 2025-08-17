package com.trm.cryptosphere.core.util

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.intercept.Interceptor
import coil.memory.MemoryCache
import okio.Path.Companion.toPath

fun imageLoader(context: Context, interceptors: Set<Interceptor> = emptySet()): ImageLoader =
  ImageLoader.Builder(context)
    .components { interceptors.forEach(::add) }
    .memoryCache { MemoryCache.Builder(context).maxSizePercent(percent = 0.25).build() }
    .diskCache {
      DiskCache.Builder()
        .directory(context.cacheDir.absolutePath.toPath().resolve("coil_cache"))
        .build()
    }
    .build()
