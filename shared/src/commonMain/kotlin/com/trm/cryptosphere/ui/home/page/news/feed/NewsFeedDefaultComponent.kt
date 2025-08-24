package com.trm.cryptosphere.ui.home.page.news.feed

import com.arkivanov.decompose.ComponentContext
import com.trm.cryptosphere.core.ui.TokenCarouselConfig
import com.trm.cryptosphere.domain.usecase.GetNews

class NewsFeedDefaultComponent(
  componentContext: ComponentContext,
  private val getNews: GetNews,
  override val onTokenCarouselItemClick: (String, TokenCarouselConfig) -> Unit,
) : NewsFeedComponent, ComponentContext by componentContext {}
