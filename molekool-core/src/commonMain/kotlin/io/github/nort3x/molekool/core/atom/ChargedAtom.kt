package io.github.nort3x.molekool.core.atom

import io.github.nort3x.molekool.core.geomertry.point.Point

open class ChargedAtom(position: Point, mass: Double, val charge: Double, type: Int) : Atom(position, mass, type) {

    override fun copy(): ChargedAtom =
        ChargedAtom(position.copy(), mass, charge, type)

    override fun toString(): String {
        return "ChargedAtom(charge=$charge, atom=${super.toString()})"
    }
}
