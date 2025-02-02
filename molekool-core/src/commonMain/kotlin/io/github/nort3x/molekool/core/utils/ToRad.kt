package io.github.nort3x.molekool.core.utils

import kotlin.math.PI

private const val RAD_CONVERTER = PI / 180.0
fun Number.toRad(): Double =
    this.toDouble() * RAD_CONVERTER
