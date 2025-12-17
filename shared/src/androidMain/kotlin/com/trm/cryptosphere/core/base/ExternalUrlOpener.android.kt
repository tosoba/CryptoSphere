package com.trm.cryptosphere.core.base

import android.content.Intent
import androidx.core.net.toUri
import com.trm.cryptosphere.core.PlatformContext

actual fun PlatformContext.openUrl(url: String) {
  startActivity(Intent(Intent.ACTION_VIEW, url.toUri()).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
}
