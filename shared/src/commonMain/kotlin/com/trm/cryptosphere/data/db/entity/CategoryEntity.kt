package com.trm.cryptosphere.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
  @PrimaryKey val id: String,
  val name: String,
  val title: String,
  val description: String,
)
