package com.trm.cryptosphere.domain.repository

import com.trm.cryptosphere.domain.model.CategoryItem

interface CategoryRepository {
  suspend fun getCategories(): List<CategoryItem>
}
