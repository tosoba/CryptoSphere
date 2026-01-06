package com.trm.cryptosphere.ui.home.page.prices

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.retainedInstance
import com.trm.cryptosphere.core.base.AppCoroutineDispatchers
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.repository.TokenRepository

class PricesDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenClick: (Int, TokenCarouselConfig) -> Unit,
  private val tokenRepository: TokenRepository,
  private val dispatchers: AppCoroutineDispatchers,
) : PricesComponent, ComponentContext by componentContext {
  override val viewModel: PricesViewModel = retainedInstance {
    PricesViewModel(tokenRepository = tokenRepository, dispatchers = dispatchers)
  }
}
