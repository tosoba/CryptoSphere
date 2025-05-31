package com.trm.cryptosphere

interface Platform {
  val name: String
}

expect fun getPlatform(): Platform
