package io.github.nort3x.molekool.bind.lammps

import io.github.nort3x.molekool.core.atom.ChargedAtom
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.geomertry.point.Point
import java.util.Scanner

fun readLampsInputFile(content: String): Sequence<List<String>> =
    content
        .substring(content.indexOf("Atoms"))
        .lineSequence()
        .filterNot { it.isBlank() }
        .filter { it[0].isDigit() }
        .map { it.split(" ").filterNot { it.isBlank() } }
        .map { it }


fun readLampsFullFile(content: String) = readLampsInputFile(content)
    .map {
        val index = it[0]
        val molecule = it[1]
        val atomType = it[2]
        val charge = it[3]
        val x = it[4].toDouble()
        val y = it[5].toDouble()
        val z = it[6].toDouble()
        molecule.toInt() to ChargedAtom(Point(x, y, z), 1.0, charge.toDouble(), atomType.toInt())
    }
    .groupBy { it.first }
    .map { Molecule(it.value.map { it.second }.toMutableList(), type = it.key) }
