package com.iskportal.molekule.core.lattice

import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.core.geomertry.point.times
import com.iskportal.molekule.core.lattice.unit.UnitCell


fun Sequence<Point>.usingUnitCell(unitCell: UnitCell): Sequence<Point> =
    map {
        with(it) {
            with(unitCell) {
                (x * a1) + (y * a2) + (z * a3)
            }
        }
    }
