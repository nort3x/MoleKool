package io.github.nort3x.molekool.core.test

import io.github.nort3x.molekool.core.atom.Atom
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.geomertry.point.times
import io.github.nort3x.molekool.core.test.utils.H2O
import io.github.nort3x.molekool.core.test.utils.shouldBeAlmostBe
import io.kotest.matchers.doubles.shouldBeLessThan
import kotlin.test.Test

class RepositionTest {

    @Test
    fun `reposition test`() {
        val a1 = Atom(Point.xHat, 0.0, 1)
        val a2 = Atom(-Point.xHat, 0.0, 2)
        val m = Molecule(
            mutableListOf(
                a1,
                a2
            )
        )

        (m.position - Point.origin).norm shouldBeLessThan 10e-5

        // move center of mass to (0,2,0)
        m.baseTo(2 * Point.yHat)

        a1.position shouldBeAlmostBe Point(1, 2, 0)
        a2.position shouldBeAlmostBe Point(-1, 2, 0)
    }

    @Test
    fun `random move test`() {
        val a1 = Atom(Point.xHat, 0.0, 1)
        val a2 = Atom(-Point.xHat, 0.0, 2)
        val a3 = Atom(Point.zHat, 0.0, 2)

        val p1BeforeMove = a1.position

        val m = Molecule(
            mutableListOf(
                a1,
                a3,
                a2
            )
        )

        (0..100).map { Point.randomOrientation }
            .flatMap { listOf(it, -it) }.shuffled()
            .forEach {
                m.move(it)
            }

        p1BeforeMove shouldBeAlmostBe a1.position
    }


    @Test
    fun `move molecule test`() {
        val water = H2O(Point.xHat)

        water.position shouldBeAlmostBe Point.xHat
        water.baseTo(Point.zHat)

        water.position shouldBeAlmostBe Point.zHat
    }

}
