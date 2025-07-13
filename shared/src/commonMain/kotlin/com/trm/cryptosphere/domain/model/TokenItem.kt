package com.trm.cryptosphere.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class TokenItem(
  val id: Int,
  val name: String,
  val symbol: String,
  val slug: String,
  val numMarketPairs: Int,
  val dateAdded: String,
  val tags: List<String>,
  val maxSupply: Double?,
  val circulatingSupply: Double,
  val totalSupply: Double,
  val infiniteSupply: Boolean,
  val platform: TokenPlatform?,
  val cmcRank: Int,
  val selfReportedCirculatingSupply: Double?,
  val selfReportedMarketCap: Double?,
  val tvlRatio: Double?,
  val lastUpdated: String,
  val quote: TokenQuote,
)

fun mockTokenItems(): List<TokenItem> =
  listOf(
    TokenItem(
      id = 1,
      name = "Bitcoin",
      symbol = "BTC",
      slug = "bitcoin",
      numMarketPairs = 12253,
      dateAdded = "2010-07-13T00:00:00.000Z",
      tags = listOf("mineable", "pow", "sha-256", "store-of-value", "state-channel"),
      maxSupply = 21000000.0,
      circulatingSupply = 19891696.0,
      totalSupply = 19891696.0,
      infiniteSupply = false,
      platform = null,
      cmcRank = 1,
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 117846.43492418,
          volume24h = 42497165433.17,
          volumeChange24h = -0.19,
          percentChange1h = 0.05, // Sample data, not in provided JSON
          percentChange24h = -0.19,
          percentChange7d = 2.5, // Sample data, not in provided JSON
          percentChange30d = 10.2, // Sample data, not in provided JSON
          percentChange60d = 15.0, // Sample data, not in provided JSON
          percentChange90d = 30.5, // Sample data, not in provided JSON
          marketCap = 2344000000000.0, // Calculated: circulatingSupply * price
          marketCapDominance = 52.8, // Sample data, not in provided JSON
          fullyDilutedMarketCap = 2474775133407.78, // Calculated: maxSupply * price
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 2,
      name = "Litecoin",
      symbol = "LTC",
      slug = "litecoin",
      numMarketPairs = 1385,
      dateAdded = "2013-04-28T00:00:00.000Z",
      tags = listOf("mineable", "pow", "scrypt", "medium-of-exchange", "layer-1"),
      maxSupply = 84000000.0,
      circulatingSupply = 76069014.48347135,
      totalSupply = 84000000.0,
      infiniteSupply = false,
      platform =
        TokenPlatform(
          id = 2502,
          name = "Huobi Token",
          slug = "htx-token",
          symbol = "HT",
          tokenAddress = "0xecb56cf772b5c9a6907fb7d32387da2fcbfb63b4",
        ),
      cmcRank = 20, // Sample Rank, actual can vary
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 94.45763733,
          volume24h = 436579423.64,
          volumeChange24h = 1.11,
          percentChange1h = 0.12, // Sample data
          percentChange24h = 1.11,
          percentChange7d = -2.3, // Sample data
          percentChange30d = 8.1, // Sample data
          percentChange60d = 12.5, // Sample data
          percentChange90d = 25.0, // Sample data
          marketCap = 7185000000.0, // Calculated: circulatingSupply * price
          marketCapDominance = 0.32, // Sample data
          fullyDilutedMarketCap = 7934441535.72, // Calculated: maxSupply * price
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 3,
      name = "Namecoin",
      symbol = "NMC",
      slug = "namecoin",
      numMarketPairs = 7,
      dateAdded = "2013-04-28T00:00:00.000Z",
      tags = listOf("mineable", "pow", "sha-256", "platform"),
      maxSupply = 21000000.0, // Typical for Bitcoin forks, not specified, using common value
      circulatingSupply = 14736400.0,
      totalSupply = 14736400.0,
      infiniteSupply = false,
      platform = null,
      cmcRank = 800, // Sample Rank
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 1.22857614,
          volume24h = 4656.37,
          volumeChange24h = 1.61,
          percentChange1h = 0.03, // Sample data
          percentChange24h = 1.61,
          percentChange7d = 0.5, // Sample data
          percentChange30d = 3.0, // Sample data
          percentChange60d = -2.0, // Sample data
          percentChange90d = 7.8, // Sample data
          marketCap = 18100000.0, // Calculated
          marketCapDominance = 0.0008, // Sample data
          fullyDilutedMarketCap = 25799098.94, // Calculated
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 4,
      name = "Terracoin",
      symbol = "TRC",
      slug = "terracoin",
      numMarketPairs = 7,
      dateAdded = "2013-04-28T00:00:00.000Z",
      tags = listOf("mineable", "pow", "sha-256", "masternodes"),
      maxSupply = 42000000.0, // Common max supply for older coins, not specified
      circulatingSupply = 0.0, // As per "0 in circulation"
      totalSupply = 35039019.14956619,
      infiniteSupply = false,
      platform = null,
      cmcRank = 1500, // Sample Rank
      selfReportedCirculatingSupply = 35038960.65,
      selfReportedMarketCap = 495548.1410176276,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 0.01414195,
          volume24h = 0.00,
          volumeChange24h = -0.20,
          percentChange1h = 0.00, // Sample data
          percentChange24h = -0.20,
          percentChange7d = -1.5, // Sample data
          percentChange30d = -5.0, // Sample data
          percentChange60d = 1.2, // Sample data
          percentChange90d = 0.5, // Sample data
          marketCap = 0.0, // Calculated (circulatingSupply * price)
          marketCapDominance = 0.0, // Sample data
          fullyDilutedMarketCap =
            594000.0, // Calculated (totalSupply * price, using selfReported for circulating)
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 5,
      name = "Peercoin",
      symbol = "PPC",
      slug = "peercoin",
      numMarketPairs = 42,
      dateAdded = "2013-04-28T00:00:00.000Z",
      tags = listOf("mineable", "hybrid-pow-pos", "sha-256", "medium-of-exchange"),
      maxSupply = null, // Peercoin has an inflationary model, typically represented as null
      circulatingSupply = 29663781.66153835,
      totalSupply = 29663781.66153835, // Often same as circulating for PPC
      infiniteSupply = true, // Due to its inflationary nature
      platform =
        TokenPlatform(
          id = 1027,
          name = "Ethereum",
          slug = "ethereum",
          symbol = "ETH",
          tokenAddress = "0x044d078F1c86508e13328842Cc75AC021B272958",
        ),
      cmcRank = 600, // Sample Rank
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 0.30738101,
          volume24h = 4070.66,
          volumeChange24h = -0.39,
          percentChange1h = -0.05, // Sample data
          percentChange24h = -0.39,
          percentChange7d = 3.1, // Sample data
          percentChange30d = 1.0, // Sample data
          percentChange60d = 4.5, // Sample data
          percentChange90d = -2.2, // Sample data
          marketCap = 9120000.0, // Calculated
          marketCapDominance = 0.0004, // Sample data
          fullyDilutedMarketCap = 9120000.0, // Same as marketCap as maxSupply is null/infinite
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 6,
      name = "Novacoin",
      symbol = "NVC",
      slug = "novacoin",
      numMarketPairs = 4,
      dateAdded = "2013-04-28T00:00:00.000Z",
      tags = listOf("mineable", "hybrid-pow-pos", "scrypt", "bnb-chain-ecosystem"),
      maxSupply = null, // Often null for PoS coins if not explicitly capped
      circulatingSupply = 0.0, // As per "0 in circulation"
      totalSupply = 4526390.522174,
      infiniteSupply = true, // Typically for PoS coins without a hard cap
      platform =
        TokenPlatform(
          id = 1839,
          name = "BNB",
          slug = "bnb",
          symbol = "BNB",
          tokenAddress = "0xbf84720097de111a80f46f9d077643967042841a",
        ),
      cmcRank = 2000, // Sample Rank
      selfReportedCirculatingSupply = 5106060.19,
      selfReportedMarketCap = 132392.07855834218,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 0.02592691,
          volume24h = 2.13,
          volumeChange24h = -0.20,
          percentChange1h = 0.01, // Sample
          percentChange24h = -0.20,
          percentChange7d = -5.0, // Sample
          percentChange30d = 10.0, // Sample
          percentChange60d = 2.0, // Sample
          percentChange90d = -8.0, // Sample
          marketCap = 0.0, // Calculated
          marketCapDominance = 0.0, // Sample
          fullyDilutedMarketCap = 132392.07, // Using selfReportedMarketCap as reference
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 7,
      name = "Devcoin",
      symbol = "DVC",
      slug = "devcoin",
      numMarketPairs = 1, // Deduced from context, not explicitly in the summary
      dateAdded = "2013-04-28T00:00:00.000Z",
      tags = listOf("mineable", "SHA-256", "Philanthropy"), // Using self_reported_tags
      maxSupply = null, // Devcoin has a complex generation model, often considered infinite
      circulatingSupply = 15167257500.0,
      totalSupply = 15167257500.0,
      infiniteSupply = true,
      platform = null,
      cmcRank = 3000, // Sample Rank
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 0.00002298,
          volume24h = 10.0, // Sample volume, not in summary
          volumeChange24h = 0.00,
          percentChange1h = 0.00, // Sample
          percentChange24h = 0.00,
          percentChange7d = 0.1, // Sample
          percentChange30d = -0.5, // Sample
          percentChange60d = 0.2, // Sample
          percentChange90d = 1.0, // Sample
          marketCap = 348500.0, // Calculated
          marketCapDominance = 0.0, // Sample
          fullyDilutedMarketCap = 348500.0, // Calculated
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 8,
      name = "Feathercoin",
      symbol = "FTC",
      slug = "feathercoin",
      numMarketPairs = 12,
      dateAdded = "2013-05-03T00:00:00.000Z",
      tags = listOf("mineable", "pow", "neoscrypt", "medium-of-exchange"),
      maxSupply = 336000000.0,
      circulatingSupply = 236600238.0,
      totalSupply = 336000000.0,
      infiniteSupply = false,
      platform = null,
      cmcRank = 1200, // Sample Rank
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 0.00235699,
          volume24h = 16.37,
          volumeChange24h = -0.14,
          percentChange1h = -0.02, // Sample
          percentChange24h = -0.14,
          percentChange7d = 1.2, // Sample
          percentChange30d = -3.0, // Sample
          percentChange60d = 0.8, // Sample
          percentChange90d = 2.5, // Sample
          marketCap = 557600.0, // Calculated
          marketCapDominance = 0.0, // Sample
          fullyDilutedMarketCap = 792000.0, // Calculated
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 9,
      name = "Mincoin",
      symbol = "MNC",
      slug = "mincoin",
      numMarketPairs = 2, // Deduced from context
      dateAdded = "2013-05-03T00:00:00.000Z",
      tags = listOf("mineable", "pow", "scrypt"),
      maxSupply = 10000000.0, // Common for older scrypt coins, not specified
      circulatingSupply = 6228916.8879491,
      totalSupply = 6228916.8879491,
      infiniteSupply = false,
      platform = null,
      cmcRank = 2500, // Sample Rank
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 0.07458228,
          volume24h = 100.0, // Sample volume
          volumeChange24h = 0.00,
          percentChange1h = 0.00, // Sample
          percentChange24h = 0.00,
          percentChange7d = 0.5, // Sample
          percentChange30d = 2.0, // Sample
          percentChange60d = -1.0, // Sample
          percentChange90d = 4.0, // Sample
          marketCap = 464500.0, // Calculated
          marketCapDominance = 0.0, // Sample
          fullyDilutedMarketCap = 745822.8, // Calculated
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
    TokenItem(
      id = 10,
      name = "Freicoin",
      symbol = "FRC",
      slug = "freicoin",
      numMarketPairs = 1,
      dateAdded = "2013-05-03T00:00:00.000Z",
      tags = listOf("mineable", "pow", "sha-256"),
      maxSupply = 100000000.0,
      circulatingSupply = 0.0, // As per "0 in circulation"
      totalSupply = 100000000.0,
      infiniteSupply = false,
      platform = null,
      cmcRank = 4000, // Sample Rank
      selfReportedCirculatingSupply = null,
      selfReportedMarketCap = null,
      tvlRatio = null,
      lastUpdated = "2025-07-13T10:16:02.801Z",
      quote =
        TokenQuote(
          price = 0.02234799,
          volume24h = 0.00,
          volumeChange24h = 0.00,
          percentChange1h = 0.00, // Sample
          percentChange24h = 0.00,
          percentChange7d = 0.0, // Sample
          percentChange30d = 0.0, // Sample
          percentChange60d = 0.0, // Sample
          percentChange90d = 0.0, // Sample
          marketCap = 0.0, // Calculated
          marketCapDominance = 0.0, // Sample
          fullyDilutedMarketCap = 2234799.0, // Calculated
          tvl = null,
          lastUpdated = "2025-07-13T10:16:02.801Z",
        ),
    ),
  )
