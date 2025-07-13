package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TokenUrls(
  val website: List<String>?,
  val twitter: List<String>?,
  @SerialName("message_board") val messageBoard: List<String>?,
  val chat: List<String>?,
  val facebook: List<String>?,
  val explorer: List<String>?,
  val reddit: List<String>?,
  @SerialName("technical_doc") val technicalDoc: List<String>?,
  @SerialName("source_code") val sourceCode: List<String>?,
  val announcement: List<String>?,
)
