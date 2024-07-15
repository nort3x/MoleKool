package com.iskportal.molekule.core.format

import com.iskportal.molekule.core.geomertry.Triangle

expect class STL constructor() {
    fun parseSTLData(byteArray: ByteArray): List<Triangle>
}