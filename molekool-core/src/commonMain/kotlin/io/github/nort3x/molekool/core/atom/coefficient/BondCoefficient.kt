package io.github.nort3x.molekool.core.atom.coefficient

class BondCoefficient(vararg coefficients: Double, type: Int) : Coefficient(*coefficients, type = type)
