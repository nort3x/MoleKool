package com.iskportal.molekule.core.geomertry

import com.iskportal.molekule.core.geomertry.point.Point

interface Distanceable {
    fun distance(point: Point): Double
}
