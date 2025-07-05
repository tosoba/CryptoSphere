package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.domain.model.TokenCarouselItem

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  override val onTokenCarouselItemClick: (String, List<TokenCarouselItem>) -> Unit,
) : NewsFeedComponent, ComponentContext by componentContext {}
