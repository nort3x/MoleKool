package io.github.nort3x.molekool.core.utils

import kotlin.math.PI

private const val degConverter = 180.0 / PI

fun Number.toDeg(): Double =
    this.toDouble() * degConverter