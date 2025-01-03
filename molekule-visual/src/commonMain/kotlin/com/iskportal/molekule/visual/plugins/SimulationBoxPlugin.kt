package com.iskportal.molekule.visual.plugins

import de.fabmax.kool.scene.addLineMesh
import de.fabmax.kool.util.Color
import com.iskportal.molekule.core.Environment
import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.visual.KoolVisualizer
import com.iskportal.molekule.visual.utils.vec3

class SimulationBoxPlugin(private val color: Color = Color.CYAN): KoolVizualizerPlugin {
    override fun initialize(koolVisualizer: KoolVisualizer) {
    }

    override fun addEnvironment(koolVisualizer: KoolVisualizer, environment: com.iskportal.molekule.core.Environment) {

        val points = with(environment.enclosingBox) {
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
                Point(xHigh, yLow, zHigh) to Point(xHigh, yHigh, zHigh)
            )
        }

        koolVisualizer.scene.addLineMesh {
            color = this@SimulationBoxPlugin.color
            points.forEach {
                addLine(it.first.vec3, it.second.vec3)
            }
        }
    }
}