package io.github.nort3x.molekool.core.geomertry.point

fun Iterable<Point>.average(): Point {
    var acc = Point.origin
    var count = 0
    for (p in this) {
        acc += p
        count++
    }
    return acc / count
}
