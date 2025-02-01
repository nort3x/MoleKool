package io.github.nort3x.molekool.core.geomertry

import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.geomertry.point.times
import kotlin.math.sqrt

data class Plane(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double
) {

    val normalVector = Point(a, b, c).normalize()

    class HessianNormalForm(
        val p: Double,
        val n: Point
    )

    fun reflect(point: Point): Point {
        val distanceToTranslate = 2 * distanceFrom(point)
        return if (point dot normalVector >= 0)
            point + (distanceToTranslate * normalVector)
        else
            point - (distanceToTranslate * normalVector)
    }

    fun distanceFrom(point: Point): Double = with(hessianNormalForm) {
        (n dot point) + p
    }

    val hessianNormalForm: HessianNormalForm = run {
        val denominator = sqrt(a * a + b * b + c * c)
        val p = d / denominator
        val n = Point(a, b, c) / denominator
        HessianNormalForm(p, n)
    }

    companion object {
        fun from(normalVector: Point, basePoint: Point): Plane {
            // n.(x-x0) = 0
            val n = normalVector.normalize()
            val d = -(n dot basePoint)
            return Plane(n.x, n.y, n.z, d)
        }
    }
}

infix fun Point.distanceFrom(plane: Plane): Double = plane.distanceFrom(this)