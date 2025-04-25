package io.github.nort3x.molekool.core.atom

open class Dihedral(vararg val atoms: Atom, override val type: Int) : MultiAtomEntity() {
    override val subAtoms: Array<out Atom>
        get() = atoms
}
