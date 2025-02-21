package io.github.nort3x.molekool.core.atom

import io.github.nort3x.molekool.core.geomertry.point.Point

open class Atom(override var position: Point, val mass: Double, override val type: Int) : Trackable, EntityGenerator,
    Moveable, Cloneable<Atom> {

    override fun generate(): Array<Trackable> = arrayOf(this)

    override fun baseTo(newPosition: Point) {
        position = newPosition
    }

    override fun copy(): Atom =
        Atom(position.copy(), mass, type)


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Atom) return false

        if (position != other.position) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = position.hashCode()
        result = 31 * result + type
        return result
    }

    override fun toString(): String {
        return "Atom(position=$position, mass=$mass, type=$type)"
    }
}
