package io.github.nort3x.molekool.visual.plugins

import de.fabmax.kool.scene.addLineMesh
import de.fabmax.kool.util.Color
import io.github.nort3x.molekool.core.geomertry.Box
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.visual.KoolVisualizer
import io.github.nort3x.molekool.visual.utils.vec3

class BoxViewerPlugin(val box: Box, val color: Color = Color.WHITE, showPlanes: Boolean = false) :
    KoolVizualizerPlugin {
    override fun initialize(koolVisualizer: KoolVisualizer) {
        val points = with(box) {
            listOf(
                Point(xLow, yLow, zLow) to Point(xHigh, yLow, zLow),
                Point(xLow, yLow, zLow) to Point(xLow, yHigh, zLow),
                Point(xLow, yLow, zLow) to Point(xLow, yLow, zHigh),

                Point(xHigh, yHigh, zLow) to Point(xHigh, yLow, zLow),
                Point(xHigh, yHigh, zLow) to Point(xLow, yHigh, zLow),
                Point(xHigh, yHigh, zLow) to Point(xHigh, yHigh, zHigh),

                Point(xLow, yHigh, zHigh) to Point(xLow, yLow, zHigh),
                Point(xLow, yHigh, zHigh) to Point(xLow, yHigh, zLow),
                Point(xLow, yHigh, zHigh) to Point(xHigh, yHigh, zHigh),

                Point(xHigh, yLow, zHigh) to Point(xLow, yLow, zHigh),
                Point(xHigh, yLow, zHigh) to Point(xHigh, yLow, zLow),
                Point(xHigh, yLow, zHigh) to Point(xHigh, yHigh, zHigh),
            )
        }

        koolVisualizer.scene.addLineMesh {
            color = this@BoxViewerPlugin.color
            points.forEach {
                addLine(it.first.vec3, it.second.vec3)
            }
        }
    }

    override fun addEnvironment(koolVisualizer: KoolVisualizer, environment: io.github.nort3x.molekool.core.Environment) {
    }
}
