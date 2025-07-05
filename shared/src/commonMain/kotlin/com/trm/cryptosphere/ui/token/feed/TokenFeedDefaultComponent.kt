package com.trm.cryptosphere.ui.token.feed

import com.arkivanov.decompose.ComponentContext

class TokenFeedDefaultComponent(componentContext: ComponentContext, private val symbol: String) :
  TokenFeedComponent, ComponentContext by componentContext {}
