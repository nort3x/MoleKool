package com.iskportal.molekule.core.atom.coefficient

import com.iskportal.molekule.core.atom.EntityGenerator
import com.iskportal.molekule.core.atom.Trackable

open class Coefficient(vararg val coefficients: Double, override val type: Int) : Trackable, EntityGenerator {
    override fun generate(): Array<Trackable> = arrayOf(this)

}