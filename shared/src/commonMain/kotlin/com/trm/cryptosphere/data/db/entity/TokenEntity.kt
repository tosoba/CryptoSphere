package com.trm.cryptosphere.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.trm.cryptosphere.data.db.entity.embedded.TokenQuoteEmbedded

@Entity(tableName = "token", indices = [Index(value = ["name", "symbol"])])
data class TokenEntity(
  @PrimaryKey val id: Int,
  @ColumnInfo(collate = ColumnInfo.NOCASE) val name: String,
  @ColumnInfo(collate = ColumnInfo.NOCASE) val symbol: String,
  val slug: String,
  @ColumnInfo(name = "num_market_pairs") val numMarketPairs: Int,
  @ColumnInfo(name = "date_added") val dateAdded: String,
  @ColumnInfo(name = "max_supply") val maxSupply: Double?,
  @ColumnInfo(name = "circulating_supply") val circulatingSupply: Double,
  @ColumnInfo(name = "total_supply") val totalSupply: Double,
  @ColumnInfo(name = "infinite_supply") val infiniteSupply: Boolean,
  @ColumnInfo(name = "cmc_rank") val cmcRank: Int,
  @ColumnInfo(name = "self_reported_circulating_supply") val selfReportedCirculatingSupply: Double?,
  @ColumnInfo(name = "self_reported_market_cap") val selfReportedMarketCap: Double?,
  @ColumnInfo(name = "tvl_ratio") val tvlRatio: Double?,
  @ColumnInfo(name = "last_updated") val lastUpdated: String,
  @Embedded(prefix = "usd_quote_") val usdQuote: TokenQuoteEmbedded,
)
