package io.github.nort3x.molekool.core.lattice.crystal.site

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.lattice.crystal.Crystal
import io.github.nort3x.molekool.core.lattice.unit.UnitCell

abstract class SiteBasedCrystal(unitCell: UnitCell) : Crystal<Point>(unitCell)
