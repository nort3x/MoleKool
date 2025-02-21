package io.github.nort3x.molekool.core.lattice.crystal

import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.atom.Atom
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.geomertry.point.length
import io.github.nort3x.molekool.core.lattice.crystal.site.Famous
import io.github.nort3x.molekool.core.lattice.crystal.site.Graphene
import io.github.nort3x.molekool.core.lattice.crystal.site.SiteBasedCrystal
import io.github.nort3x.molekool.core.lattice.unit.UnitCell
import io.github.nort3x.molekool.core.lattice.unit.bravis.Bravais3D

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

        fun fromEnvironmentAtoms(env: Environment): Crystal<Atom> {
            val box = env.enclosingBox
            return object : Crystal<Atom>(
                Bravais3D.orthorhombic(
                    box.xBoundary.length,
                    box.yBoundary.length,
                    box.zBoundary.length,
                )
            ) {
                override fun generate(basePoint: Point): List<Atom> =
                    env.atoms.map {
                        it.copy().apply { move(basePoint) }
                    }
            }
        }

        fun fromEnvironmentMolecules(env: Environment): Crystal<Molecule> {
            val box = env.enclosingBox
            return object : Crystal<Molecule>(
                Bravais3D.orthorhombic(
                    box.xBoundary.length,
                    box.yBoundary.length,
                    box.zBoundary.length,
                )
            ) {
                override fun generate(basePoint: Point): List<Molecule> =
                    env.molecules.map {
                        it.copy().apply { move(basePoint) }
                    }
            }
        }


        val graphene = Graphene
        val famous = Famous
    }
}
