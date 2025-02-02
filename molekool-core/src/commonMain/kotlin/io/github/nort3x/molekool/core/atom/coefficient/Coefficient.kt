package io.github.nort3x.molekool.core.atom.coefficient

import io.github.nort3x.molekool.core.atom.EntityGenerator
import io.github.nort3x.molekool.core.atom.Trackable

open class Coefficient(vararg val coefficients: Double, override val type: Int) : Trackable, EntityGenerator {
    override fun generate(): Array<Trackable> = arrayOf(this)
}
