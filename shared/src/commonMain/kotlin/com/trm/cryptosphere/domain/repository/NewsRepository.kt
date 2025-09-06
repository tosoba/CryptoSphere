package com.trm.cryptosphere.domain.repository

import androidx.paging.PagingData
import com.trm.cryptosphere.domain.model.NewsItem
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
  fun getNewsFlow(): Flow<PagingData<NewsItem>>
}
