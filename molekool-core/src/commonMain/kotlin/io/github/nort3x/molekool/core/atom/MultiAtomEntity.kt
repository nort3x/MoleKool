package io.github.nort3x.molekool.core.atom

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.geomertry.point.average

abstract class MultiAtomEntity(
    vararg val subAtoms: Atom,
) : Trackable, EntityGenerator, Moveable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MultiAtomEntity) return false

        if (!subAtoms.contentEquals(other.subAtoms)) return false

        return true
    }

    override fun hashCode(): Int {
        return subAtoms.contentHashCode()
    }

    override fun generate(): Array<Trackable> = arrayOf(this, *subAtoms)
    override fun toString(): String {
        return "MultiAtomEntity(subAtoms=${subAtoms.contentToString()})"
    }

    override val position: Point
        get() = subAtoms.map { it.position }.average()

    override fun baseTo(newPosition: Point) {
        val com = position
        // r = x - com
        for (subAtom in subAtoms) {
            val r = subAtom.position - com
            subAtom.baseTo(r + newPosition)
        }
    }
}
