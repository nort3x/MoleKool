package com.iskportal.molekule.core.atom.coefficient

class BondCoefficient(vararg coefficients: Double, type: Int) : Coefficient(*coefficients, type = type)