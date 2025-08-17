package com.trm.cryptosphere.di

import android.content.Context
import com.trm.cryptosphere.core.ui.ColorExtractor
import com.trm.cryptosphere.core.util.imageLoader

class AndroidDependencyContainer(context: Context) {
  val colorExtractor = ColorExtractor(imageLoader(context), context)
}
