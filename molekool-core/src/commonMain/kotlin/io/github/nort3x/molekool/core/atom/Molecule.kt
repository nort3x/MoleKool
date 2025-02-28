package io.github.nort3x.molekool.core.atom

open class Molecule(
    val atoms: MutableList<Atom> = mutableListOf(),
    val bonds: MutableList<Bond> = mutableListOf(),
    val angles: MutableList<Angle> = mutableListOf(),
    val dihedral: MutableList<Dihedral> = mutableListOf(),
    override val type: Int = 0,
) : MultiAtomEntity(), EntityGenerator, Cloneable<Molecule> {

    override fun generate(): Array<Trackable> =
        (atoms + bonds + angles + dihedral)
            .flatMap { it.generate().asSequence() }
            .plus(this)
            .distinct()
            .toTypedArray()


    override val subAtoms: Array<Atom>
        get() = generate().filterIsInstance<Atom>().toTypedArray()

    override fun copy(): Molecule {
        val atomsWithIndex = subAtoms.mapIndexed { index, atom -> atom to index }.toMap()
        val copiedAtoms = atomsWithIndex.map { it.value to it.key.copy() }.toMap()

        fun findAtomFromCopy(atom: Atom): Atom = copiedAtoms[atomsWithIndex[atom]!!]!!

        return Molecule(
            copiedAtoms.values.toMutableList(),
            bonds.map { Bond(findAtomFromCopy(it.first), findAtomFromCopy(it.second), it.type) }.toMutableList(),
            angles.map {
                Angle(
                    findAtomFromCopy(it.first),
                    findAtomFromCopy(it.second),
                    findAtomFromCopy(it.third),
                    it.type
                )
            }.toMutableList(),
            dihedral.map { Dihedral(*it.subAtoms.map(::findAtomFromCopy).toTypedArray(), type = it.type) }
                .toMutableList(),
            type
        )
    }
}
