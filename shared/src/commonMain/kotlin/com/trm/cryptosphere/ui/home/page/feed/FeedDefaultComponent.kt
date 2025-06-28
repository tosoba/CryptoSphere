package com.trm.cryptosphere.ui.home.page.feed

import com.arkivanov.decompose.ComponentContext

class FeedDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenClick: (String) -> Unit,
) : FeedComponent, ComponentContext by componentContext {}
