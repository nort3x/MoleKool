package io.github.nort3x.molekool.core.test.utils

import io.github.nort3x.molekool.core.AtomicMass
import io.github.nort3x.molekool.core.atom.Angle
import io.github.nort3x.molekool.core.atom.Bond
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.compounds.atomOf
import io.github.nort3x.molekool.core.geomertry.point.Point

class H2O(p: Point) : Molecule() {
    init {
        val h1 = atomOf(AtomicMass.H, p + (Point.xHat * 0.2))
        val h2 = atomOf(AtomicMass.H, p - (Point.xHat * 0.2))
        val o1 = atomOf(AtomicMass.H, p)

        val b1 = Bond(h1, o1, 1)
        val b2 = Bond(h2, o1, 1)

        val a1 = Angle(h1, o1, h2, 1)

        angles.add(a1)
        bonds.addAll(listOf(b1, b2))
        atoms.addAll(listOf(h1, h2, o1))
    }
}
