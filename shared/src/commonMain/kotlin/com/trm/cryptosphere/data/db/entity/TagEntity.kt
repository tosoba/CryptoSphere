package com.trm.cryptosphere.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tag") data class TagEntity(@PrimaryKey val name: String)
