/*
showing how to apply STL files and creating a closed surface from them
*/

import com.iskportal.molekule.core.AtomicMass
import com.iskportal.molekule.core.Environment
import com.iskportal.molekule.core.compounds.atomOf
import com.iskportal.molekule.core.format.STL
import com.iskportal.molekule.core.geomertry.TriangleMeshClosedSurface
import com.iskportal.molekule.core.geomertry.isInside
import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.core.lattice.Grid3D
import com.iskportal.molekule.core.lattice.spanInAllDirections
import com.iskportal.molekule.visual.KoolVisualizer
import com.iskportal.molekule.visual.plugins.AxisPlugin
import com.iskportal.molekule.visual.plugins.BoxViewerPlugin

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

