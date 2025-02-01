package io.github.nort3x.molekool.core.lattice.crystal

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.lattice.crystal.site.Famous
import io.github.nort3x.molekool.core.lattice.crystal.site.Graphene
import io.github.nort3x.molekool.core.lattice.crystal.site.SiteBasedCrystal
import io.github.nort3x.molekool.core.lattice.unit.UnitCell

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
