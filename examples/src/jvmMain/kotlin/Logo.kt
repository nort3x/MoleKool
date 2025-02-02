import io.github.nort3x.molekool.core.AtomicMass
import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.compounds.atomOf
import io.github.nort3x.molekool.core.format.STL
import io.github.nort3x.molekool.core.geomertry.*
import io.github.nort3x.molekool.core.geomertry.point.Point
import io.github.nort3x.molekool.core.geomertry.point.length
import io.github.nort3x.molekool.core.lattice.Grid2D
import io.github.nort3x.molekool.core.lattice.Grid3D
import io.github.nort3x.molekool.core.lattice.crystal.Crystal
import io.github.nort3x.molekool.core.lattice.crystal.usingCrystal
import io.github.nort3x.molekool.core.lattice.spanInAllDirections
import io.github.nort3x.molekool.core.utils.toRad
import io.github.nort3x.molekool.visual.KoolVisualizer
import io.github.nort3x.molekool.visual.plugins.BoxViewerPlugin

fun main() {

    // your world
    val env = Environment()

    // read monkey stl
    val data = STL(STL::class.java.classLoader.getResource("monkey.stl")!!.readBytes())
    val monkeyFace = TriangleMeshClosedSurface(data.triangles)

    // sample inside monkey
    Grid3D(20, 20, 20)
        .points.spanInAllDirections()
        .filter { it isInside monkeyFace }
        .map { it.rotateX(90.toRad()) }
        .map { it.copy(z = -it.z) }
        .map { atomOf(AtomicMass.Fe, it) }
        .forEach { env.add(it) }

    // update box based on monkey face
    env.boundingBox = with(env.enclosingBox) {
        val edgeLen = listOf(xBoundary.length, yBoundary.length, zBoundary.length).max() * 1.5
        Box(-edgeLen to edgeLen, -edgeLen to edgeLen, -edgeLen to edgeLen)
    }

    // sphere to cut out from
    val sphere = Sphere(Point(0, 0, 0), 0.2 * env.enclosingBox.xBoundary.length)

    // a famous grid
    Grid2D(50, 50)
        .points
        .spanInAllDirections()
        .usingCrystal(Crystal.graphene.armchair(1.0))
        .flatMap { listOf(it, it.rotateZ(5.6.toRad())) }
        .filter { it isInside env.boundingBox!! }
        .filter { it isOutside sphere }
        .map { atomOf(AtomicMass.H, it) }
        .forEach { env.add(it) }

    // visualize
    KoolVisualizer()
        .withDefaultConfig()
        .addPlugin(BoxViewerPlugin(env.enclosingBox))
        .init()
        .addEnvironment(env)
        .runAndWaitUntilExit()

}
