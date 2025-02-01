package io.github.nort3x.molekool.core.utils

infix fun Double.outside(range: Pair<Number, Number>) =
    this < range.first.toDouble() || this > range.second.toDouble()