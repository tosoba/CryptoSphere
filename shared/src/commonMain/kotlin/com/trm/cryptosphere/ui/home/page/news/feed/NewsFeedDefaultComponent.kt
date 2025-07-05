package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenClick: (String) -> Unit,
) : NewsFeedComponent, ComponentContext by componentContext {}
