package com.trm.cryptosphere.data.db.mapper

import com.trm.cryptosphere.data.api.coinmarketcap.model.CmcCategoryItem
import com.trm.cryptosphere.data.db.entity.CategoryEntity
import com.trm.cryptosphere.domain.model.CategoryItem

fun CmcCategoryItem.toEntity(): CategoryEntity =
  CategoryEntity(id = id, name = name, title = title, description = description)

fun CategoryEntity.toCategoryItem(): CategoryItem =
  CategoryItem(id = id, name = name, title = title, description = description)
