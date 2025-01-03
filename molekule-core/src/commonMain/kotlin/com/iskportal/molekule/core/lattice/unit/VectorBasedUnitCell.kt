package com.iskportal.molekule.core.lattice.unit

import com.iskportal.molekule.core.geomertry.point.Point

class VectorBasedUnitCell(
    override val a1: Point,
    override val a2: Point,
    override val a3: Point
) : UnitCell {
    override fun toString(): String = listOf(a1, a2, a3).toString()
}
