@file:Suppress("unused") // Used in Swift

package com.trm.cryptosphere.core.util

import dev.icerock.moko.resources.PluralsResource
import dev.icerock.moko.resources.StringResource
import dev.icerock.moko.resources.desc.PluralFormatted
import dev.icerock.moko.resources.desc.Resource
import dev.icerock.moko.resources.desc.ResourceFormatted
import dev.icerock.moko.resources.desc.StringDesc

fun getString(stringResource: StringResource): StringDesc = StringDesc.Resource(stringResource)

fun getString(stringResource: StringResource, parameter: Any): StringDesc =
  StringDesc.ResourceFormatted(stringResource, parameter)

fun getPluralFormatted(pluralResource: PluralsResource, quantity: Int): StringDesc =
  StringDesc.PluralFormatted(pluralResource, quantity, quantity)
