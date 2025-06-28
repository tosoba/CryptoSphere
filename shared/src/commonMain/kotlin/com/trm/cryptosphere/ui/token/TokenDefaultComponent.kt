package com.trm.cryptosphere.ui.token

import com.arkivanov.decompose.ComponentContext

class TokenDefaultComponent(componentContext: ComponentContext, private val symbol: String) :
  TokenComponent, ComponentContext by componentContext {}
