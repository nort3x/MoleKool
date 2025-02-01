/*
showing how to apply STL files and creating a closed surface from them
*/

import io.github.nort3x.molekool.core.AtomicMass
import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.compounds.atomOf
import io.github.nort3x.molekool.core.format.STL
import io.github.nort3x.molekool.core.geomertry.TriangleMeshClosedSurface
import io.github.nort3x.molekool.core.geomertry.isInside
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.lattice.Grid3D
import io.github.nort3x.molekool.core.lattice.spanInAllDirections
import io.github.nort3x.molekool.visual.KoolVisualizer
import io.github.nort3x.molekool.visual.plugins.AxisPlugin
import io.github.nort3x.molekool.visual.plugins.BoxViewerPlugin

fun main() {

    // reading bytes in a painful way - go ahead provide byte array however you can
    val data = STL::class.java.classLoader.getResource("monkey.stl")!!.readBytes()

    // parse to triangles
    val triangles = with(STL()) { parseSTLData(data) }

    // closed surface from mesh-triangles
    val shape = TriangleMeshClosedSurface(triangles)

    val env = Environment()

    Grid3D(30, 30, 30)
        .points
        .spanInAllDirections()
        .filter { it isInside shape }
        .map { it + Point.xHat * 20 }
        .map { atomOf(AtomicMass.Fe, it) }
        .forEach { env.add(it) }

    KoolVisualizer()
        .withDefaultConfig()
        .addPlugin(BoxViewerPlugin(env.enclosingBox))
        .addPlugin(AxisPlugin(relativeSizeMultiplier = 1.2f))
        .init()
        .addEnvironment(env)
        .runAndWaitUntilExit()
}

