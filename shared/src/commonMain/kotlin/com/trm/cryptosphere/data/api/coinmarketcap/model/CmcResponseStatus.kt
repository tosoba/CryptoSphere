package com.trm.cryptosphere.data.api.coinmarketcap.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CmcResponseStatus(
  val timestamp: String?,
  @SerialName("error_code") val errorCode: Int?,
  @SerialName("error_message") val errorMessage: String?,
  val elapsed: Int?,
  @SerialName("credit_count") val creditCount: Int?,
  val notice: String?,
  @SerialName("total_count") val totalCount: Int?,
)
