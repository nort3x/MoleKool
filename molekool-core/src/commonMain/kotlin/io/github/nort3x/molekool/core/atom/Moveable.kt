package io.github.nort3x.molekool.core.atom

import io.github.nort3x.molekool.core.geomertry.point.Point

interface Moveable {
    val position: Point
    fun baseTo(newPosition: Point)
    fun move(delta: Point) = baseTo(position + delta)
}
