package io.github.nort3x.molekool.bind.lammps

import io.github.nort3x.molekool.bind.plus
import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.atom.ChargedAtom
import java.io.File
import java.io.FileOutputStream

enum class AtomStyle {
    ATOMIC,
    MOLECULE,
    FULL,
}

fun Environment.toLammpsInputFile(
    filePath: String,
    atomStyle: AtomStyle = AtomStyle.ATOMIC,
    enclosingBoxOffset: Double = 0.0,
    rescale: Double = 1.0,
) {
    println("generating input file from ${this.entities.size} entities")

    val labeledAtoms = this.atoms.toList().mapIndexed { index, atom -> atom to index + 1 }.toMap()
    println("generated labeled atoms")
    val labeledBonds = this.bond.toList().mapIndexed { index, atom -> atom to index + 1 }.toMap()
    println("generated labeled bonds")
    val labeledAngles = this.angles.toList().mapIndexed { index, atom -> atom to index + 1 }.toMap()
    println("generated labeled angles")
    val labeledDihedral = this.dihedral.toList().mapIndexed { index, atom -> atom to index + 1 }.toMap()
    println("generated labeled dihedrals")
    val labeledMolecule = this.molecules.toList().mapIndexed { index, atom -> atom to index + 1 }.toMap()
    println("generated labeled molecules")

    val moleculeAtomsSet = labeledMolecule.keys.flatMap { it.atoms }.toSet()
    val moleculeLessAtoms = labeledAtoms.filterNot { (atom, _) -> moleculeAtomsSet.contains(atom) }
    println("generated molecule-less Atoms")


    FileOutputStream(File(filePath)).use { oups ->

        oups + "# generated with molekool-gen"
        oups + ""
        oups + "${this.atoms.size}\tatoms"
        oups + "${this.bond.size}\tbonds"
        oups + "${this.angles.size}\tangles"
        oups + "${this.dihedral.size}\tdihedrals"
        oups + ""
//        oups + "${this.atoms.distinctBy { it.type }.size}\t\tatom types"
        oups + "${this.atoms.maxOf { it.type }}\t\tatom types"
        oups + "${this.bond.distinctBy { it.type }.size}\t\tbond types"
        oups + "${this.angles.distinctBy { it.type }.size}\t\tangle types"
        oups + "${this.dihedral.distinctBy { it.type }.size}\t\tdihedral types"
        oups + ""

        val box = this.enclosingBox(enclosingBoxOffset)
        oups + "${box.xLow} ${box.xHigh} xlo xhi"
        oups + "${box.yLow} ${box.yHigh} ylo yhi"
        oups + "${box.zLow} ${box.zHigh} zlo zhi"
        oups + ""
        oups + "Masses"
        oups + ""
        (1 until  this.atoms.minOf { it.type }).forEach{
            oups + "$it 0.0"
        }
        this.atoms.distinctBy { it.type }
            .sortedBy { it.type }
            .forEach {
                oups + "${it.type} ${it.mass}"
            }

        if (this.bondCoefficients.isNotEmpty()) {
            oups + ""
            oups + "Bond Coeffs"
            oups + ""
            this.bondCoefficients
                .distinctBy { it.type }
                .forEach {
                    oups + "${it.type} ${it.coefficients.joinToString("\t")}"
                }
        }

        if (this.angleCoefficients.isNotEmpty()) {
            oups + ""
            oups + "Angle Coeffs"
            oups + ""
            this.angleCoefficients
                .distinctBy { it.type }
                .forEach {
                    oups + "${it.type} ${it.coefficients.joinToString("\t")}"
                }
        }

        if (this.dihedralCoefficients.isNotEmpty()) {
            oups + ""
            oups + "Dihedral Coeffs"
            oups + ""
            this.dihedralCoefficients
                .distinctBy { it.type }
                .forEach {
                    oups + "${it.type} ${it.coefficients.joinToString("\t")}"
                }
        }

        oups + ""
        oups + "Atoms"
        oups + ""

        when (atomStyle) {
            AtomStyle.ATOMIC -> {
                labeledAtoms
                    .entries.sortedBy { it.value }
                    .forEach { (atom, index) ->
                        oups + "$index ${atom.type} ${atom.position.x * rescale} ${atom.position.y * rescale} ${atom.position.z * rescale}"
                    }
            }

            AtomStyle.MOLECULE -> {
                moleculeLessAtoms.forEach { (atom, index) ->
                    oups + "$index 0 ${atom.type} ${atom.position.x * rescale} ${atom.position.y * rescale} ${atom.position.z * rescale}"
                }
                labeledMolecule
                    .forEach { (molecule, moleculeIndex) ->
                        molecule.atoms.map { it to labeledAtoms[it] }
                            .sortedBy { it.second }
                            .forEach { (atom, atomIndex) ->
                                oups + "$atomIndex $moleculeIndex ${atom.type} ${atom.position.x * rescale} ${atom.position.y * rescale} ${atom.position.z * rescale}"
                            }
                    }
            }

            AtomStyle.FULL -> {
                moleculeLessAtoms.forEach { (atom, index) ->
                    oups + "$index 0 ${atom.type} ${if (atom is ChargedAtom) atom.charge else 0.0} ${atom.position.x * rescale} ${atom.position.y * rescale} ${atom.position.z * rescale}"
                }
                labeledMolecule
                    .forEach { (molecule, moleculeIndex) ->
                        molecule.atoms
                            .map { it to labeledAtoms[it] }
                            .sortedBy { it.second }
                            .forEach { (chargedAtom, atomIndex) ->
                                oups + "$atomIndex $moleculeIndex ${chargedAtom.type} ${if (chargedAtom is ChargedAtom) chargedAtom.charge else 0.0} ${chargedAtom.position.x * rescale} ${chargedAtom.position.y * rescale} ${chargedAtom.position.z * rescale}"
                            }
                    }
            }
        }

        if (labeledBonds.isNotEmpty()) {
            oups + ""
            oups + "Bonds"
            oups + ""
            labeledBonds.entries.sortedBy { it.value }.forEach { (bond, index) ->
                oups + "$index ${bond.type} ${labeledAtoms[bond.first]!!} ${labeledAtoms[bond.second]!!}"
            }
        }

        if (labeledAngles.isNotEmpty()) {
            oups + ""
            oups + "Angles"
            oups + ""
            labeledAngles.entries.sortedBy { it.value }.forEach { (angle, index) ->
                oups + "$index ${angle.type} ${labeledAtoms[angle.first]!!} ${labeledAtoms[angle.second]!!} ${labeledAtoms[angle.third]!!}"
            }
        }

        if (labeledDihedral.isNotEmpty()) {
            oups + ""
            oups + "Dihedrals"
            oups + ""
            labeledDihedral.entries.sortedBy { it.value }.forEach { (dihedral, index) ->
                oups + "$index ${dihedral.type} ${dihedral.subAtoms.map { labeledAtoms[it]!! }.joinToString(" ")}"
            }
        }
    }
}

fun Environment.toLammpsDumpFile(
    filePath: String,
    timeStamps: Long = 0,
    bounds: String = "pp pp pp",
    enclosingBoxOffset: Double = 0.0,
    rescale: Double = 1.0,
) {
    val labeledAtoms = this.atoms.mapIndexed { index, atom -> atom to index + 1 }.toMap()
    val box = enclosingBox(enclosingBoxOffset)
    FileOutputStream(File(filePath)).use { oups ->

        oups + """
        ITEM: TIMESTEP
        $timeStamps
        ITEM: NUMBER OF ATOMS
        ${labeledAtoms.size}
        ITEM: BOX BOUNDS $bounds
        ${box.xLow * rescale} ${box.xHigh * rescale}
        ${box.yLow * rescale} ${box.yHigh * rescale}
        ${box.zLow * rescale} ${box.zHigh * rescale}
        ITEM: ATOMS id type xs ys zs
        """.trimIndent()
        labeledAtoms.forEach { (atom, index) ->
            oups + "$index ${atom.type} ${atom.position.x * rescale} ${atom.position.y * rescale} ${atom.position.z * rescale}"
        }
    }
}
