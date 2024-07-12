package com.iskportal.molekule.core.utils

import com.iskportal.molekule.core.geomertry.Box
import com.iskportal.molekule.core.geomertry.isInside
import com.iskportal.molekule.core.geomertry.isOnSurface
import com.iskportal.molekule.core.geomertry.point.Point

infix fun Point.isInPeriodicRegion(box: Box): Boolean =
    this isInside box || (this isOnSurface box && this.x < box.xHigh && this.y < box.yHigh && this.z < box.zHigh)