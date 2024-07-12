package com.iskportal.molekule.core.lattice.crystal.site

import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.core.lattice.crystal.Crystal
import com.iskportal.molekule.core.lattice.unit.UnitCell

abstract class SiteBasedCrystal(unitCell: UnitCell) : Crystal<Point>(unitCell)
