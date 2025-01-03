package com.iskportal.molekule.core.lattice.crystal

import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.core.lattice.crystal.site.Famous
import com.iskportal.molekule.core.lattice.crystal.site.Graphene
import com.iskportal.molekule.core.lattice.crystal.site.SiteBasedCrystal
import com.iskportal.molekule.core.lattice.unit.UnitCell

/**
 * @param T: anything that is generated in this crystal
 */
abstract class Crystal<T>(val unitCell: UnitCell) {
    /**
     * @param basePoint: given this point generate list of items to be used in next stage
     */
    abstract fun generate(basePoint: Point): List<T>

    companion object {
        fun fromUnitCell(cell: UnitCell): SiteBasedCrystal =
            object : SiteBasedCrystal(cell) {
                override fun generate(basePoint: Point): List<Point> = listOf(basePoint)
            }


        val graphene = Graphene
        val famous = Famous
    }
}
