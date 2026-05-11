package com.trm.cryptosphere.di

import android.content.Context
import com.trm.cryptosphere.core.base.imageLoader
import com.trm.cryptosphere.core.cache.util.cachePath
import com.trm.cryptosphere.core.ui.theme.ColorExtractor

class AndroidDependencyContainer(context: Context) {
  val colorExtractor =
    ColorExtractor(
      imageLoader = imageLoader(context = context, cacheDir = context.cachePath),
      context = context,
    )
}
