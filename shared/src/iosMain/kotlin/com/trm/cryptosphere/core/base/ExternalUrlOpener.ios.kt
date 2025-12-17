package com.trm.cryptosphere.core.base

import com.trm.cryptosphere.core.PlatformContext
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun PlatformContext.openUrl(url: String) {
  val nsUrl = NSURL(string = url)
  if (UIApplication.sharedApplication.canOpenURL(nsUrl)) {
    UIApplication.sharedApplication.openURL(nsUrl)
  }
}
