/*
showing how to parse STL file and traverse triangles
*/

import io.github.nort3x.molekool.core.AtomicMass
import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.compounds.atomOf
import io.github.nort3x.molekool.core.format.STL
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.visual.KoolVisualizer
import io.github.nort3x.molekool.visual.plugins.AxisPlugin
import io.github.nort3x.molekool.visual.plugins.BoxViewerPlugin

fun main() {

    // reading bytes in a painful way - go ahead provide byte array however you can
    val data = STL::class.java.classLoader.getResource("monkey.stl")!!.readBytes()

    // parse to triangles
    val triangles = with(STL()) { parseSTLData(data) }


    val env = Environment()

    triangles
        .map { it.middle }

        // uncomment flatmap and comment map to switch between middle - vertices
//        .flatMap {
//            // three vertices of triangle
//            listOf(it.a, it.b, it.c)
//        }
        .map { it + Point.xHat * 20 }
        .map { atomOf(AtomicMass.Li, it) }
        .forEach { env.add(it) }

    KoolVisualizer()
        .withDefaultConfig()
        .addPlugin(BoxViewerPlugin(env.enclosingBox))
        .addPlugin(AxisPlugin(relativeSizeMultiplier = 1.2f))
        .init()
        .addEnvironment(env)
        .runAndWaitUntilExit()
}

