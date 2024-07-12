package com.iskportal.molekule.visual.utils

import de.fabmax.kool.math.MutableVec3f
import com.iskportal.molekule.core.geomertry.point.Point

val Point.vec3: MutableVec3f
    get() {
        val vec = MutableVec3f(x.toFloat(), y.toFloat(), z.toFloat())
        return vec
    }