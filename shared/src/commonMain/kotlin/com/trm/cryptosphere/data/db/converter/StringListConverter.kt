package com.trm.cryptosphere.data.db.converter

import androidx.room.TypeConverter

class StringListConverter {
  @TypeConverter fun toList(value: String): List<String> = value.split(",").map(String::trim)

  @TypeConverter fun toString(list: List<String>): String = list.joinToString(",")
}
