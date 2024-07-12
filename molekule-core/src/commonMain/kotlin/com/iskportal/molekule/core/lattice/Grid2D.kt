package com.iskportal.molekule.core.lattice

import com.iskportal.molekule.core.geomertry.point.Point

class Grid2D(width: Int, height: Int) : Lattice {
    private val thinGrid = Grid3D(width, height, 1)
    override val points: Sequence<Point>
        get() = thinGrid.points

}
