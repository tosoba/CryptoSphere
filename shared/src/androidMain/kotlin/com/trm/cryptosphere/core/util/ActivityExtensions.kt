package com.trm.cryptosphere.core.util

import android.app.Activity
import com.trm.cryptosphere.di.DependencyContainer
import com.trm.cryptosphere.di.DependencyContainerHolder

val Activity.container: DependencyContainer
  get() = (application as DependencyContainerHolder).dependencyContainer
