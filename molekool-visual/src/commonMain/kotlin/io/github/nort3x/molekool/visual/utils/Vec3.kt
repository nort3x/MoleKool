package io.github.nort3x.molekool.visual.utils

import de.fabmax.kool.math.MutableVec3f
import io.github.nort3x.molekool.core.geomertry.point.Point

val Point.vec3: MutableVec3f
    get() {
        val vec = MutableVec3f(x.toFloat(), y.toFloat(), z.toFloat())
        return vec
    }
