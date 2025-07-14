package com.trm.cryptosphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenCarouselItem(val id: String, val symbol: String, val imageUrl: String?)

fun mockTokenCarouselItems(): List<TokenCarouselItem> =
  listOf(
    TokenCarouselItem("bitcoin", "BTC", "https://static.coinstats.app/coins/1650455588819.png"),
    TokenCarouselItem("ethereum", "ETH", "https://static.coinstats.app/coins/1650455629727.png"),
    TokenCarouselItem("tether", "USDT", "https://static.coinstats.app/coins/1650455771843.png"),
    TokenCarouselItem("bitcoin", "BTC", "https://static.coinstats.app/coins/1650455588819.png"),
    TokenCarouselItem("ethereum", "ETH", "https://static.coinstats.app/coins/1650455629727.png"),
    TokenCarouselItem("tether", "USDT", "https://static.coinstats.app/coins/1650455771843.png"),
  )
