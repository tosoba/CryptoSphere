package com.trm.cryptosphere.data.db.converter

import androidx.room.TypeConverter
import com.trm.cryptosphere.api.coinmarketcap.model.TokenQuote
import kotlinx.serialization.json.Json

class TokenQuoteConverter {
  @TypeConverter
  fun fromTokenQuote(tokenQuote: TokenQuote): String = Json.encodeToString(tokenQuote)

  @TypeConverter
  fun toTokenQuote(tokenQuoteString: String): TokenQuote =
    Json.decodeFromString<TokenQuote>(tokenQuoteString)
}
