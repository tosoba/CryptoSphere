package com.trm.cryptosphere.data.repository

import com.trm.cryptosphere.data.store.CategoryStore
import com.trm.cryptosphere.domain.model.CategoryItem
import com.trm.cryptosphere.domain.repository.CategoryRepository
import org.mobilenativefoundation.store.store5.impl.extensions.get

class CategoryNetworkRepository(private val store: CategoryStore) : CategoryRepository {
  override suspend fun getCategories(): List<CategoryItem> = store.get(0)
}
