package io.github.nort3x.molekool.core.utils

import io.github.nort3x.molekool.core.geomertry.Box
import io.github.nort3x.molekool.core.geomertry.isInside
import io.github.nort3x.molekool.core.geomertry.isOnSurface
import io.github.nort3x.molekool.core.geomertry.point.Point

infix fun Point.isInPeriodicRegion(box: Box): Boolean =
    this isInside box || (this isOnSurface box && this.x < box.xHigh && this.y < box.yHigh && this.z < box.zHigh)