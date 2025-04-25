package io.github.nort3x.molekool.core.test

import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.lattice.crystal.Crystal
import io.github.nort3x.molekool.core.lattice.crystal.usingCrystal
import io.github.nort3x.molekool.core.test.utils.H2O
import io.github.nort3x.molekool.core.test.utils.shouldBeAlmostBe
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.types.shouldNotBeSameInstanceAs
import kotlin.test.Test

class EnvironmentCrystalTest {
    @Test
    fun `environment molecule crystal can re-produce`() {
        val env = Environment()

        env.add(H2O(Point.xHat))

        val asList = sequenceOf(Point.origin)
            .usingCrystal(Crystal.fromEnvironmentMolecules(env))
            .toList()

        asList shouldHaveSize 1

        with(asList.first()) {
            position shouldBeAlmostBe Point.xHat
            atoms shouldHaveSize 3
            bonds shouldHaveSize 2
            angles shouldHaveSize 1

            generate().forEach { l ->
                env.molecules.first().generate().forEach { r ->
                    l shouldNotBeSameInstanceAs r
                }
            }
        }
    }

    @Test
    fun `environment molecule crystal line`() {
        val env = Environment()

        env.add(H2O(Point.origin))

        val asList = (0 until 11).map { Point(0, it, 0) }.asSequence()
            .usingCrystal(Crystal.fromEnvironmentMolecules(env))
            .toList()

        asList.forEachIndexed { i, molecule ->
            molecule.position shouldBeAlmostBe Point(i, 0, 0)
        }
    }
}
