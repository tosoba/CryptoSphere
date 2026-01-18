package com.trm.cryptosphere.ui.token.feed

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.trm.cryptosphere.core.base.fullDecimalFormat
import com.trm.cryptosphere.core.base.shortDecimalFormat
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.shared.MR
import dev.icerock.moko.resources.StringResource

data class TokenFeedParameter(
  val label: StringResource,
  val value: Number,
  val valueFormat: (Number) -> String = { "$${it.toDouble().shortDecimalFormat()}" },
  val valueChange: Number? = null,
  val valueChangeFormat: ((Number?) -> String)? = null,
)

@Composable
fun rememberTokenFeedParameters(token: TokenItem): List<TokenFeedParameter> =
  remember(token) { tokenFeedParameters(token) }

fun tokenFeedParameters(token: TokenItem): List<TokenFeedParameter> {
  val valueChangeFormat: (Number?) -> String = {
    it
      ?.toDouble()
      ?.fullDecimalFormat(significantDecimals = 2, signed = true)
      ?.let { formatted -> " $formatted% " }
      .orEmpty()
  }
  return listOfNotNull(
    TokenFeedParameter(
      label = MR.strings.price,
      value = token.quote.price,
      valueFormat = { "$${it.toDouble().fullDecimalFormat()}" },
      valueChange = token.quote.percentChange24h,
      valueChangeFormat = valueChangeFormat,
    ),
    TokenFeedParameter(
      label = MR.strings.volume_24h,
      value = token.quote.volume24h,
      valueChange = token.quote.volumeChange24h,
      valueChangeFormat = valueChangeFormat,
    ),
    TokenFeedParameter(label = MR.strings.market_cap, value = token.quote.marketCap),
    TokenFeedParameter(label = MR.strings.circulating_supply, value = token.circulatingSupply),
    TokenFeedParameter(label = MR.strings.total_supply, value = token.totalSupply),
    token.maxSupply?.let { TokenFeedParameter(label = MR.strings.max_supply, value = it) },
  )
}
