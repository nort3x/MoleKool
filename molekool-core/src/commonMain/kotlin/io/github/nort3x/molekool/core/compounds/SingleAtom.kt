package io.github.nort3x.molekool.core.compounds

import io.github.nort3x.molekool.core.AtomicMass
import io.github.nort3x.molekool.core.atom.Atom
import io.github.nort3x.molekool.core.geomertry.point.Point

fun atomOf(atomicMass: io.github.nort3x.molekool.core.AtomicMass, position: Point, type: Int = atomicMass.atomType): Atom =
    Atom(position, atomicMass.mass, type)
