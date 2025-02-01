package io.github.nort3x.molekool.core.geomertry

import io.github.nort3x.molekool.core.geomertry.point.Point
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan

data class Triangle(
    val a: Point,
    val b: Point,
    val c: Point
) {

    val middle: Point = (a + b + c) / 3.0

    val area: Double

    /*
    for this triangle
    a
    | \c
    b/
    it faces out of your screen (right hand rule)
    ab x ac
     */
    val normal: Point

    init {
        val ab = b - a
        val ac = c - a
        val face = (ab cross ac)
        area = face.norm * 0.5
        normal = face.normalize()
    }

    val plane: Plane = Plane.from(normal, a)


    /**
     * returns solid angle of this triangle seen from [point] as it's origin - sign indicates you are looking parallel (positive) or anti-parallel (negative)
     * see [Tetrahedron SolidAngle Formula](https://en.wikipedia.org/wiki/Solid_angle#Tetrahedron)
     */
    fun solidAngleFrom(point: Point): Double {
        val a0 = a - point
        val b0 = b - point
        val c0 = c - point
        val m0 = middle - point

        val aNorm = a0.norm
        val bNorm = b0.norm
        val cNorm = c0.norm

        // see Eriksson, Folke (1990). "On the measure of solid angles". Math. Mag. 63 (3): 184–187. doi:10.2307/2691141. JSTOR 2691141
        val tanHalfAngle = a0.dot(b0 cross c0).absoluteValue / (
                aNorm * bNorm * cNorm +
                        (a0 dot b0) * cNorm +
                        (a0 dot c0) * bNorm +
                        (b0 dot c0) * aNorm
                )

        var angle = atan(tanHalfAngle) * 2
        angle = if(angle < 0.0) angle + PI else angle

        // normal face sign condition (parallel and anti-parallel)
        return if ((m0 dot normal) < 0) -angle
        else angle
    }

    operator fun times(n: Number) = Triangle(a*n, b*n, c*n)
}

operator fun Number.times(t: Triangle) = Triangle(t.a*this, t.b*this, t.c*this)