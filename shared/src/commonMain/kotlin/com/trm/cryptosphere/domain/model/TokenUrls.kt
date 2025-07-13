package com.trm.cryptosphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenUrls(
  val website: List<String>?,
  val twitter: List<String>?,
  val messageBoard: List<String>?,
  val chat: List<String>?,
  val facebook: List<String>?,
  val explorer: List<String>?,
  val reddit: List<String>?,
  val technicalDoc: List<String>?,
  val sourceCode: List<String>?,
  val announcement: List<String>?,
)
