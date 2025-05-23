package io.github.nort3x.molekool.core.lattice.crystal.site

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.lattice.unit.VectorBasedUnitCell
import io.github.nort3x.molekool.core.utils.toRad
import kotlin.math.sqrt

object Graphene {
    fun zigzag(latticeConstant: Double): SiteBasedCrystal {
        val cell = VectorBasedUnitCell(
            Point(latticeConstant, 0, 0) + Point(latticeConstant, 0, 0).rotateZ(60.toRad()),
            Point(latticeConstant, 0, 0) + Point(latticeConstant, 0, 0).rotateZ((-60).toRad()),
            Point.zHat,
        )
        return object : SiteBasedCrystal(cell) {
            override fun generate(basePoint: Point): List<Point> =
                listOf(basePoint, basePoint + Point(latticeConstant, 0, 0))
        }
    }

    fun armchair(latticeConstant: Double): SiteBasedCrystal {
        val cell = VectorBasedUnitCell(
            Point(latticeConstant * sqrt(3.0), 0, 0),
            Point(latticeConstant * sqrt(3.0), 0, 0).rotateZ(120.toRad()),
            Point.zHat,
        )
        return object : SiteBasedCrystal(cell) {
            override fun generate(basePoint: Point): List<Point> {
                return listOf(basePoint, basePoint + Point(0, latticeConstant, 0))
            }
        }
    }
}
