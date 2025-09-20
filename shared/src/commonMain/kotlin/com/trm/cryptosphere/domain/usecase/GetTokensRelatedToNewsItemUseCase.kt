package com.trm.cryptosphere.domain.usecase

import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.model.TokenItem
import com.trm.cryptosphere.domain.repository.TokenRepository

class GetTokensRelatedToNewsItemUseCase(private val tokenRepository: TokenRepository) {
  suspend operator fun invoke(item: NewsItem): List<TokenItem> =
    tokenRepository.getTokensMatchingSearchTerms(
      buildList {
        item.searchKeyWords?.let(::addAll)
        addAll(item.relatedCoins)
      }
    )
}
