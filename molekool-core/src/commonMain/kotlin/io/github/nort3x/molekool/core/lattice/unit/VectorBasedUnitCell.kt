package io.github.nort3x.molekool.core.lattice.unit

import io.github.nort3x.molekool.core.geomertry.point.Point

class VectorBasedUnitCell(
    override val a1: Point,
    override val a2: Point,
    override val a3: Point
) : UnitCell {
    override fun toString(): String = listOf(a1, a2, a3).toString()
}
