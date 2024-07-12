package com.iskportal.molekule.core.lattice.unit

import com.iskportal.molekule.core.geomertry.point.Point

typealias Basis = Array<Point>

interface UnitCell {

    val a1: Point
    val a2: Point
    val a3: Point

    val volume: Double
        get() = (a1 cross a2) dot a3

    fun direct(point: Point): Point =
        with(point) {
            (a1 * x) + (a2 * y) + (a3 * z)
        }

    val basis: Basis
        get() = arrayOf(a1, a2, a3)
}
