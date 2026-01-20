package com.trm.cryptosphere.ui.home.page.prices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.trm.cryptosphere.core.base.fullDecimalFormat
import com.trm.cryptosphere.core.ui.ListItemImage
import com.trm.cryptosphere.core.ui.ListItemInfoColumn
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.model.TokenQuote
import com.trm.cryptosphere.domain.model.logoUrl
import kotlin.math.sign

@Composable
fun PriceItemContent(
  token: TokenItem,
  shape: Shape,
  modifier: Modifier = Modifier,
  onClick: () -> Unit,
) {
  Card(
    modifier = modifier,
    shape = shape,
    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
    onClick = onClick,
  ) {
    Row(
      modifier = Modifier.fillMaxWidth().padding(16.dp),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.SpaceBetween,
    ) {
      Text(
        text = "${token.cmcRank}",
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )

      Spacer(modifier = Modifier.width(16.dp))

      ListItemImage(token.logoUrl)

      Spacer(modifier = Modifier.width(16.dp))

      ListItemInfoColumn(topText = token.name, bottomText = token.symbol)

      Spacer(modifier = Modifier.width(16.dp))

      PriceItemTokenQuoteColumn(token)
    }
  }
}

@Composable
private fun PriceItemTokenQuoteColumn(token: TokenItem) {
  Column(horizontalAlignment = Alignment.End) {
    Text(
      text = "$${token.quote.price.fullDecimalFormat()}",
      style = MaterialTheme.typography.bodyLarge,
      color = MaterialTheme.colorScheme.onSurface,
    )

    val percentChange24h = token.quote.percentChange24h
    val valueChangePositive = percentChange24h.sign >= 0

    Box(
      contentAlignment = Alignment.Center,
      modifier =
        Modifier.background(
          color = if (valueChangePositive) Color.Green else Color.Red,
          shape = RoundedCornerShape(4.dp),
        ),
    ) {
      Text(
        text = "${percentChange24h.fullDecimalFormat(significantDecimals = 2, signed = true)}%",
        style =
          MaterialTheme.typography.bodyMedium.copy(
            fontWeight = FontWeight.Medium,
            color = if (valueChangePositive) Color.Unspecified else Color.White,
          ),
        modifier = Modifier.padding(horizontal = 4.dp),
      )
    }
  }
}

@Preview(showBackground = true)
@Composable
private fun PriceItemContentPreview() {
  PriceItemContent(
    token =
      TokenItem(
        id = 1,
        name = "Bitcoin",
        symbol = "BTC",
        slug = "bitcoin",
        numMarketPairs = 100,
        dateAdded = "2023-01-01",
        maxSupply = 21000000.0,
        circulatingSupply = 19500000.0,
        totalSupply = 19500000.0,
        infiniteSupply = false,
        cmcRank = 1,
        selfReportedCirculatingSupply = 19500000.0,
        selfReportedMarketCap = 1000000000000.0,
        tvlRatio = 1.5,
        lastUpdated = "2023-06-01T12:00:00Z",
        quote =
          TokenQuote(
            price = 30000.5,
            volume24h = 1000000000.0,
            volumeChange24h = 50000000.0,
            percentChange1h = 0.5,
            percentChange24h = 2.3,
            percentChange7d = 5.1,
            percentChange30d = 8.2,
            percentChange60d = 15.3,
            percentChange90d = 20.4,
            marketCap = 585000000000.0,
            marketCapDominance = 45.2,
            fullyDilutedMarketCap = 630000000000.0,
            tvl = 15000000000.0,
            lastUpdated = "2023-06-01T12:00:00Z",
          ),
        tagNames = listOf("cryptocurrency", "defi"),
      ),
    shape = RoundedCornerShape(8.dp),
    onClick = {},
  )
}
