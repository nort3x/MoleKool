package io.github.nort3x.molekool.core.geomertry

import io.github.nort3x.molekool.core.geomertry.point.Point

interface Distanceable {
    fun distance(point: Point): Double
}
