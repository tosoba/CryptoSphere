package com.trm.cryptosphere.data.db.mapper

import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokenItem
import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcTokenQuote
import com.trm.cryptosphere.data.db.entity.TokenEntity
import com.trm.cryptosphere.data.db.entity.embedded.TokenQuoteEmbedded
import com.trm.cryptosphere.data.db.entity.junction.TokenWithTagNamesJunction
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.TokenQuote

fun CmcTokenItem.toEntity() =
  TokenEntity(
    id = id,
    name = name,
    symbol = symbol,
    slug = slug,
    numMarketPairs = numMarketPairs,
    dateAdded = dateAdded,
    maxSupply = maxSupply,
    circulatingSupply = circulatingSupply,
    totalSupply = totalSupply,
    infiniteSupply = infiniteSupply,
    cmcRank = cmcRank,
    selfReportedCirculatingSupply = selfReportedCirculatingSupply,
    selfReportedMarketCap = selfReportedMarketCap,
    tvlRatio = tvlRatio,
    lastUpdated = lastUpdated,
    usdQuote = quote.usd.toEmbedded(),
  )

private fun CmcTokenQuote.toEmbedded() =
  TokenQuoteEmbedded(
    price = price,
    volume24h = volume24h,
    volumeChange24h = volumeChange24h,
    percentChange1h = percentChange1h,
    percentChange24h = percentChange24h,
    percentChange7d = percentChange7d,
    percentChange30d = percentChange30d,
    percentChange60d = percentChange60d,
    percentChange90d = percentChange90d,
    marketCap = marketCap,
    marketCapDominance = marketCapDominance,
    fullyDilutedMarketCap = fullyDilutedMarketCap,
    tvl = tvl,
    lastUpdated = lastUpdated,
  )

fun TokenWithTagNamesJunction.toTokenItem() =
  TokenItem(
    id = token.id,
    name = token.name,
    symbol = token.symbol,
    slug = token.slug,
    numMarketPairs = token.numMarketPairs,
    dateAdded = token.dateAdded,
    maxSupply = token.maxSupply,
    circulatingSupply = token.circulatingSupply,
    totalSupply = token.totalSupply,
    infiniteSupply = token.infiniteSupply,
    cmcRank = token.cmcRank,
    selfReportedCirculatingSupply = token.selfReportedCirculatingSupply,
    selfReportedMarketCap = token.selfReportedMarketCap,
    tvlRatio = token.tvlRatio,
    lastUpdated = token.lastUpdated,
    quote = token.usdQuote.toTokenQuote(),
    tagNames = tagNames?.split(",").orEmpty(),
  )

fun TokenEntity.toTokenItem() =
  TokenItem(
    id = id,
    name = name,
    symbol = symbol,
    slug = slug,
    numMarketPairs = numMarketPairs,
    dateAdded = dateAdded,
    maxSupply = maxSupply,
    circulatingSupply = circulatingSupply,
    totalSupply = totalSupply,
    infiniteSupply = infiniteSupply,
    cmcRank = cmcRank,
    selfReportedCirculatingSupply = selfReportedCirculatingSupply,
    selfReportedMarketCap = selfReportedMarketCap,
    tvlRatio = tvlRatio,
    lastUpdated = lastUpdated,
    quote = usdQuote.toTokenQuote(),
  )

private fun TokenQuoteEmbedded.toTokenQuote() =
  TokenQuote(
    price = price,
    volume24h = volume24h,
    volumeChange24h = volumeChange24h,
    percentChange1h = percentChange1h,
    percentChange24h = percentChange24h,
    percentChange7d = percentChange7d,
    percentChange30d = percentChange30d,
    percentChange60d = percentChange60d,
    percentChange90d = percentChange90d,
    marketCap = marketCap,
    marketCapDominance = marketCapDominance,
    fullyDilutedMarketCap = fullyDilutedMarketCap,
    tvl = tvl,
    lastUpdated = lastUpdated,
  )
