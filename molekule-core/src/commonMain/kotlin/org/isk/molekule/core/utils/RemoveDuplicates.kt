package org.isk.molekule.core.utils

import org.isk.molekule.core.geomertry.point.Point
import kotlin.math.roundToLong

fun Sequence<Point>.removeDuplicates(distanceThreshold: Double = 10e-2): Sequence<Point> =
    this.distinctBy {
        val r = Triple(
            (it.x / distanceThreshold).roundToLong(),
            (it.y / distanceThreshold).roundToLong(),
            (it.z / distanceThreshold).roundToLong()
        )
        r
    }
