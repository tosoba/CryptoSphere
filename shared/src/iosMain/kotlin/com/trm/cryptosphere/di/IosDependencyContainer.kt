package com.trm.cryptosphere.di

import com.trm.cryptosphere.core.PlatformContext
import com.trm.cryptosphere.core.base.imageLoader
import com.trm.cryptosphere.core.cache.util.cachePath
import com.trm.cryptosphere.core.ui.theme.ColorExtractor

class IosDependencyContainer(context: PlatformContext) {
  val colorExtractor =
    ColorExtractor(
      imageLoader =
        imageLoader(context = coil3.PlatformContext.INSTANCE, cacheDir = context.cachePath),
      context = coil3.PlatformContext.INSTANCE,
    )
}
