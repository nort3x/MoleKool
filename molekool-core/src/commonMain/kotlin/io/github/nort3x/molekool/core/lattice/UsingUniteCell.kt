package io.github.nort3x.molekool.core.lattice

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.geomertry.point.times
import io.github.nort3x.molekool.core.lattice.unit.UnitCell


fun Sequence<Point>.usingUnitCell(unitCell: UnitCell): Sequence<Point> =
    map {
        with(it) {
            with(unitCell) {
                (x * a1) + (y * a2) + (z * a3)
            }
        }
    }
