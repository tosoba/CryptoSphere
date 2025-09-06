package com.trm.cryptosphere.domain.usecase

import androidx.paging.PagingData
import com.trm.cryptosphere.domain.model.NewsItem
import com.trm.cryptosphere.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow

class GetNews(private val newsRepository: NewsRepository) {
  operator fun invoke(): Flow<PagingData<NewsItem>> = newsRepository.getNewsFlow()
}
