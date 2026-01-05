package com.trm.cryptosphere.core.base

import com.trm.cryptosphere.core.PlatformContext
import platform.Foundation.NSURL
import platform.UIKit.UIActivityViewController
import platform.UIKit.UIApplication

actual fun PlatformContext.shareUrl(url: String) {
  UIApplication.sharedApplication.keyWindow
    ?.rootViewController
    ?.presentViewController(
      UIActivityViewController(
        activityItems = listOf(NSURL(string = url)),
        applicationActivities = null,
      ),
      animated = true,
      completion = null,
    )
}
