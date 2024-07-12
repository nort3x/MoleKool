package com.iskportal.molekule.core.compounds

import com.iskportal.molekule.core.AtomicMass
import com.iskportal.molekule.core.atom.Atom
import com.iskportal.molekule.core.geomertry.point.Point

fun atomOf(atomicMass: com.iskportal.molekule.core.AtomicMass, position: Point, type: Int = atomicMass.atomType): Atom =
    Atom(position, atomicMass.mass, type)
