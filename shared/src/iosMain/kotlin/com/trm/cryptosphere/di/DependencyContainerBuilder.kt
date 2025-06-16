package com.trm.cryptosphere.di

import com.trm.cryptosphere.core.PlatformContext

fun buildDependencyContainer(): DependencyContainer = DependencyContainer(PlatformContext.INSTANCE)
