package io.github.nort3x.molekool.core.lattice.crystal.site

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.lattice.unit.bravis.Bravais3D

object Famous {
    fun diamond(latticeConstant: Double): SiteBasedCrystal {
        val cell = Bravais3D.cubicFaceCentered(latticeConstant)
        return object : SiteBasedCrystal(cell) {
            override fun generate(basePoint: Point): List<Point> {
                return listOf(
                    basePoint,
                    basePoint + cell.direct(Point(0.25, 0.25, 0.25))
                )
            }

        }
    }
}
