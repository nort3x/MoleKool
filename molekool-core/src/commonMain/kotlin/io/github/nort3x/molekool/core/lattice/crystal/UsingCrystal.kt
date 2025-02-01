package io.github.nort3x.molekool.core.lattice.crystal

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.lattice.usingUnitCell

fun <T> Sequence<Point>.usingCrystal(crystal: Crystal<T>): Sequence<T> =
    usingUnitCell(crystal.unitCell)
        .flatMap { crystal.generate(it) }
