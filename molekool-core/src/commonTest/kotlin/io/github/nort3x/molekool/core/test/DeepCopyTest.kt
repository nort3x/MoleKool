package io.github.nort3x.molekool.core.test

import io.github.nort3x.molekool.core.AtomicMass
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.compounds.atomOf
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlin.test.Test

class DeepCopyTest {
    @Test
    fun `test copy of molecule`(){
        val molecule = Molecule(mutableListOf(atomOf(AtomicMass.He, Point.origin)))

        val moleculeCopy = molecule.copy();

        moleculeCopy shouldNotBeSameInstanceAs molecule
        moleculeCopy.atoms.first() shouldNotBeSameInstanceAs molecule.atoms.first()
    }
}
