package io.github.nort3x.molekool.core.utils

import kotlin.math.PI

private const val DEG_CONVERTER = 180.0 / PI

fun Number.toDeg(): Double =
    this.toDouble() * DEG_CONVERTER
