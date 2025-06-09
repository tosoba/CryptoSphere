package com.trm.cryptosphere.api.coinstats.model

import kotlinx.serialization.Serializable

@Serializable
data class NewsResponseItem(
  val id: String,
  val searchKeyWords: List<String>?,
  val feedDate: Long,
  val source: String,
  val title: String,
  val sourceLink: String,
  val isFeatured: Boolean?,
  val imgUrl: String?,
  val reactionsCount: Map<String, Int>?,
  val reactions: List<String>?,
  val shareURL: String,
  val relatedCoins: List<String>?,
  val content: Boolean?,
  val link: String,
  val bigImg: Boolean?,
)
