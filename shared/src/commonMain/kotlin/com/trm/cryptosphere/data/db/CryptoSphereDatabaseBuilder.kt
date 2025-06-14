package com.trm.cryptosphere.data.db

import com.trm.cryptosphere.core.PlatformContext

expect fun buildCryptoSphereDatabase(context: PlatformContext): CryptoSphereDatabase
