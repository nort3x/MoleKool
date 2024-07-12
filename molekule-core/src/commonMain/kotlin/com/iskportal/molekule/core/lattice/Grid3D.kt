package com.iskportal.molekule.core.lattice

import com.iskportal.molekule.core.geomertry.point.Point

open class Grid3D(
    val width: Int,
    val height: Int,
    val depth: Int,
) : Lattice {
    override val points: Sequence<Point>
        get() = sequence {
            for (x in 0 until width)
                for (y in 0 until height)
                    for (z in 0 until depth)
                        yield(Point(x, y, z))
        }
}
