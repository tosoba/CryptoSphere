package com.trm.cryptosphere.ui.token.details

import com.arkivanov.decompose.ComponentContext

interface TokenDetailsComponent {
  val tokenId: Int

  fun interface Factory {
    operator fun invoke(componentContext: ComponentContext, tokenId: Int): TokenDetailsComponent
  }
}
