package io.github.nort3x.molekool.core.atom

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.geomertry.point.average

abstract class MultiAtomEntity : Trackable(), EntityGenerator, Moveable {
    abstract val subAtoms: Array<out Atom>

    override fun generate(): Array<Trackable> = arrayOf(this, *subAtoms)
    override fun toString(): String {
        return "MultiAtomEntity(subAtoms=${subAtoms.contentToString()})"
    }

    override val position: Point
        get() = subAtoms.map { it.position }.average()

    override fun baseTo(newPosition: Point) {
        val centerOfMass = this.position // Calculate the center of mass once
        for (atom in subAtoms) {
            val relative = atom.position - centerOfMass // Calculate the relative position
            atom.baseTo(relative + newPosition) // Apply the new position
        }
    }
}
