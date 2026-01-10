package com.trm.cryptosphere.core.base

import com.trm.cryptosphere.core.PlatformContext
import platform.Foundation.NSURL
import platform.UIKit.UIApplication

actual fun PlatformContext.openUrl(url: String) {
  UIApplication.sharedApplication.openURL(NSURL(string = url), mapOf<Any?, Any>(), null)
}
