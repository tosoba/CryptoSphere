package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext

class TokenFeedDefaultComponent(
  componentContext: ComponentContext,
  override val symbols: List<String>,
  override val currentIndex: Int,
) : TokenFeedComponent, ComponentContext by componentContext {}
