package io.github.nort3x.molekool.bind.lammps

import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.atom.Angle
import io.github.nort3x.molekool.core.atom.Atom
import io.github.nort3x.molekool.core.atom.Bond
import io.github.nort3x.molekool.core.atom.ChargedAtom
import io.github.nort3x.molekool.core.atom.Dihedral
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.atom.coefficient.AngleCoefficient
import io.github.nort3x.molekool.core.atom.coefficient.BondCoefficient
import io.github.nort3x.molekool.core.atom.coefficient.DihedralCoefficient
import io.github.nort3x.molekool.core.geomertry.point.Point

fun readLampsSnapshotFile(content: String): Pair<Sequence<List<String>>, Sequence<List<String>>> =
    content
        .substring(content.indexOf("Atoms"), content.indexOf("Velocities"))
        .lineSequence()
        .filterNot { it.isBlank() }
        .filter { it[0].isDigit() }
        .map { it.split(" ").filterNot { it.isBlank() } }
        .map { it } to
        content.substring(content.indexOf("Velocities"))
            .lineSequence()
            .filterNot { it.isBlank() }
            .filter { it[0].isDigit() }
            .map { it.split(" ").filterNot { it.isBlank() } }
            .map { it }

data class IndexedMolecule(
    val moleculeIndex: Int,
    val atomIndex: Map<Int, Atom>,
) : Molecule(atoms = atomIndex.values.toMutableList())

fun readLampsFullFile(content: String) = with(readLampsSnapshotFile(content)) {
    first
        .map {
            val index = it[0].toInt()
            val molecule = it[1]
            val atomType = it[2]
            val charge = it[3]
            val x = it[4].toDouble()
            val y = it[5].toDouble()
            val z = it[6].toDouble()
            Pair(molecule.toInt(), index to ChargedAtom(Point(x, y, z), 1.0, charge.toDouble(), atomType.toInt()))
        }
        .groupBy { it.first }
        .map {
            val atomsWithIndex = it.value.map { it.second }.toMap()
            IndexedMolecule(it.key, atomsWithIndex)
        } to second
}

data class InfoMaps(
    val atomMap: MutableMap<Int, ChargedAtom> = mutableMapOf(),
    val moleculeMap: MutableMap<Int, Molecule> = mutableMapOf(),
    val bondMap: MutableMap<Int, Bond> = mutableMapOf(),
    val angleMap: MutableMap<Int, Angle> = mutableMapOf(),
    val dihedralMap: MutableMap<Int, Dihedral> = mutableMapOf(),
    val massMap: MutableMap<Int, Double> = mutableMapOf(),
)

fun readInputFileFull(content: String): Pair<Environment, InfoMaps> {
    val env = Environment()

    val atomMap: MutableMap<Int, ChargedAtom> = mutableMapOf()
    val moleculeMap: MutableMap<Int, Molecule> = mutableMapOf()
    val bondMap: MutableMap<Int, Bond> = mutableMapOf()
    val angleMap: MutableMap<Int, Angle> = mutableMapOf()
    val dihedralMap: MutableMap<Int, Dihedral> = mutableMapOf()

    val massMap: MutableMap<Int, Double> = mutableMapOf()

    val tokenPositionMap = InputFileTokens.entries.associateWith { content.indexOf(it.token) }
        .filterNot { it.value == -1 }.toMutableMap()

    tokenPositionMap[InputFileTokens.BEGIN] = 0
    tokenPositionMap[InputFileTokens.END] = content.length

    // initialize masses map
    run {
        tokenPositionMap[InputFileTokens.MASSES]?.let { massPos ->
            val end = tokenPositionMap.values.sorted().first { it > massPos }
            content.substring(massPos, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .forEach {
                    val type = it[0].toInt()
                    val mass = it[1].toDouble()
                    massMap[type] = mass
                }
        }
    }

    // initialize coefficients
    run {
        tokenPositionMap[InputFileTokens.BOND_COEFFS]?.let { bondCoeff ->
            val end = tokenPositionMap.values.sorted().first { it > bondCoeff }
            content.substring(bondCoeff, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .map {
                    BondCoefficient(
                        *it.drop(1).mapNotNull { it.toDoubleOrNull() }.toDoubleArray(),
                        type = it[0].toInt(),
                    )
                }
                .forEach { env.add(it) }
        }

        tokenPositionMap[InputFileTokens.ANGLE_COEFFS]?.let { angleCoeff ->
            val end = tokenPositionMap.values.sorted().first { it > angleCoeff }
            content.substring(angleCoeff, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .map {
                    AngleCoefficient(
                        *it.drop(1).mapNotNull { it.toDoubleOrNull() }.toDoubleArray(),
                        type = it[0].toInt(),
                    )
                }
                .forEach { env.add(it) }
        }

        tokenPositionMap[InputFileTokens.DIHEDRAL_COEFFS]?.let { dihedralCoeff ->
            val end = tokenPositionMap.values.sorted().first { it > dihedralCoeff }
            content.substring(dihedralCoeff, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .map {
                    DihedralCoefficient(
                        *it.drop(1).mapNotNull { it.toDoubleOrNull() }.toDoubleArray(),
                        type = it[0].toInt(),
                    )
                }
                .forEach { env.add(it) }
        }
    }

    // initialize atoms
    run {
        tokenPositionMap[InputFileTokens.ATOMS]?.let { atomPos ->
            val end = tokenPositionMap.values.sorted().first { it > atomPos }
            content.substring(atomPos, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .forEach {
                    val index = it[0].toInt()
                    val molecule = it[1].toInt()
                    val atomType = it[2].toInt()
                    val charge = it[3].toDouble()
                    val x = it[4].toDouble()
                    val y = it[5].toDouble()
                    val z = it[6].toDouble()

                    val atom = ChargedAtom(Point(x, y, z), massMap[atomType]!!, charge, atomType)
                    atomMap[index] = atom

                    val mol = moleculeMap.computeIfAbsent(molecule) { Molecule(type = 0).apply { env.add(this) } }
                    moleculeMap[molecule] = mol.apply { atoms.add(atom) }.copy()

                    env.add(atom)
                }
        }
    }

    // initialize bonds
    run {
        tokenPositionMap[InputFileTokens.BONDS]?.let { bondsPos ->
            val end = tokenPositionMap.values.sorted().first { it > bondsPos }
            content.substring(bondsPos, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .forEach {
                    val index = it[0].toInt()
                    val bondType = it[1].toInt()
                    val atom1 = it[2].toInt()
                    val atom2 = it[3].toInt()

                    val bond = Bond(atomMap[atom1]!!, atomMap[atom2]!!, bondType)
                    bondMap[index] = bond

                    env.add(bond)
                }
        }
    }

    // initialize angles
    run {
        tokenPositionMap[InputFileTokens.ANGLES]?.let { anglePos ->
            val end = tokenPositionMap.values.sorted().first { it > anglePos }
            content.substring(anglePos, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .forEach {
                    val index = it[0].toInt()
                    val angleType = it[1].toInt()
                    val atom1 = it[2].toInt()
                    val atom2 = it[3].toInt()
                    val atom3 = it[4].toInt()

                    val angle = Angle(atomMap[atom1]!!, atomMap[atom2]!!, atomMap[atom3]!!, angleType)
                    angleMap[index] = angle

                    env.add(angle)
                }
        }
    }

    // initialize dihedrals
    run {
        tokenPositionMap[InputFileTokens.DIHEDRALS]?.let { dihedralPos ->
            val end = tokenPositionMap.values.sorted().first { it > dihedralPos }
            content.substring(dihedralPos, end)
                .lineSequence()
                .drop(1)
                .filterNot { it.isBlank() }
                .map { it.split(" ", "#").filter { it.isNotBlank() } }
                .forEach {
                    val index = it[0].toInt()
                    val dihedralType = it[1].toInt()

                    val dihedral = Dihedral(
                        *it.drop(2)
                            .mapNotNull { it.toIntOrNull() }
                            .map { atomMap[it]!! }
                            .toTypedArray(),
                        type = dihedralType,
                    )
                    dihedralMap[index] = dihedral

                    env.add(dihedral)
                }
        }
    }

    return env to InfoMaps(
        atomMap,
        moleculeMap,
        bondMap,
        angleMap,
        dihedralMap,
        massMap,
    )
}
