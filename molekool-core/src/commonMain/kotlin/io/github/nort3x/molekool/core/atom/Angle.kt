package io.github.nort3x.molekool.core.atom

open class Angle(
    val first: Atom,
    val second: Atom,
    val third: Atom,
    override val type: Int,
) : MultiAtomEntity() {
    override val subAtoms: Array<Atom>
        get() = arrayOf(first, second, third)
}
