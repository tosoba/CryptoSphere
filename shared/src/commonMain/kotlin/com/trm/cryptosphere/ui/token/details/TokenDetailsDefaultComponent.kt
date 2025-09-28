package com.trm.cryptosphere.ui.token.details

import com.arkivanov.decompose.ComponentContext

class TokenDetailsDefaultComponent(
  componentContext: ComponentContext,
  override val tokenId: Int,
) : TokenDetailsComponent, ComponentContext by componentContext
