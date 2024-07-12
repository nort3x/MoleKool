package com.iskportal.molekule.core.lattice.crystal

import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.core.lattice.usingUnitCell

fun <T> Sequence<Point>.usingCrystal(crystal: Crystal<T>): Sequence<T> =
    usingUnitCell(crystal.unitCell)
        .flatMap { crystal.generate(it) }
