package io.github.nort3x.molekool.core.atom

open class Bond(
    val first: Atom,
    val second: Atom,
    override val type: Int,
) : MultiAtomEntity() {
    override val subAtoms: Array<Atom>
        get() = arrayOf(first, second)
}
