package io.github.nort3x.molekool.core.format

import io.github.nort3x.molekool.core.geomertry.Triangle

expect class STL constructor() {
    fun parseSTLData(byteArray: ByteArray): List<Triangle>
}