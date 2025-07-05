package com.trm.cryptosphere.ui.token.details

import com.arkivanov.decompose.ComponentContext

class TokenDetailsDefaultComponent(
  componentContext: ComponentContext,
  override val symbol: String,
) : TokenDetailsComponent, ComponentContext by componentContext
