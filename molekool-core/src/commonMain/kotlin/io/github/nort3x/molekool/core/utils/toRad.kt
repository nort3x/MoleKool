package io.github.nort3x.molekool.core.utils

import kotlin.math.PI

private const val radConverter = PI / 180.0
fun Number.toRad(): Double =
    this.toDouble() * radConverter