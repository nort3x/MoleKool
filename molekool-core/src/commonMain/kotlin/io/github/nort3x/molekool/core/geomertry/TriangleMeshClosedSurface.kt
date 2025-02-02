package io.github.nort3x.molekool.core.geomertry

import io.github.nort3x.molekool.core.geomertry.point.Point
import kotlin.math.PI
import kotlin.math.absoluteValue

class TriangleMeshClosedSurface(
    private val triangles: List<Triangle>,
    accuracy: Double = 10e-4,
) : ClosedSurface {

    private val upperBound = 2 * PI - accuracy
    private val lowerBound = PI + accuracy

    override fun contains(point: Point): Boolean =
        // higher than 6.28 + acc (strong indicate that this is inside)
        triangles.sumOf { it.solidAngleFrom(point) }.absoluteValue > upperBound

    override fun excludes(point: Point): Boolean =
        triangles.sumOf { it.solidAngleFrom(point) }.absoluteValue < lowerBound
}
