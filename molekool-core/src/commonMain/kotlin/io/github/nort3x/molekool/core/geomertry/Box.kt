package io.github.nort3x.molekool.core.geomertry

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.utils.between
import io.github.nort3x.molekool.core.utils.outside

data class Box(
    val xBoundary: Pair<Number, Number>,
    val yBoundary: Pair<Number, Number>,
    val zBoundary: Pair<Number, Number>,
) : ClosedSurface {

    constructor(
        xLow: Number,
        xHigh: Number,
        yLow: Number,
        yHigh: Number,
        zLow: Number,
        zHigh: Number,
    ) : this(
        xLow to xHigh,
        yLow to yHigh,
        zLow to zHigh,
    )

    val corners: List<Point>
        get() = listOf(
            Point(xLow, yLow, zLow), // 000
            Point(xLow, yLow, zHigh), // 001
            Point(xLow, yHigh, zLow), // 010
            Point(xLow, yHigh, zHigh), // 011
            Point(xHigh, yLow, zLow), // 100
            Point(xHigh, yLow, zHigh), // 101
            Point(xHigh, yHigh, zLow), // 110
            Point(xHigh, yHigh, zHigh), // 111
        )
	val xLow
        get() = xBoundary.first.toDouble()
    val xHigh
        get() = xBoundary.second.toDouble()

    val yLow
        get() = yBoundary.first.toDouble()
    val yHigh
        get() = yBoundary.second.toDouble()

    val zLow
        get() = zBoundary.first.toDouble()
    val zHigh
        get() = zBoundary.second.toDouble()

    /**
     * vector which represents lowest point of (left most inner bottom point) (xlow, ylow, zlow)
     */
    val edge: Point
        get() = Point(xLow, yLow, zLow)

    val middle: Point
        get() = (edge + Point(xHigh, yHigh, zHigh)) * 0.5

    init {
        if (xLow > xHigh) {
            throw IllegalArgumentException("x boundary is not valid: $xBoundary should from low to high and not equal")
        }

        if (yLow > yHigh) {
            throw IllegalArgumentException("y boundary is not valid: $yBoundary should from low to high and not equal")
        }

        if (zLow > zHigh) {
            throw IllegalArgumentException("z boundary is not valid: $zBoundary should from low to high and not equal")
        }
    }

    override fun contains(point: Point): Boolean =
        with(point) {
            x between xBoundary && y between yBoundary && z between zBoundary
        }

    override fun excludes(point: Point): Boolean =
        with(point) {
            x outside xBoundary || y outside yBoundary || z outside zBoundary
        }
}
