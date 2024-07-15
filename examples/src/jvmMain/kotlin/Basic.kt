import com.iskportal.molekule.core.AtomicMass
import com.iskportal.molekule.core.Environment
import com.iskportal.molekule.core.atom.Atom
import com.iskportal.molekule.core.atom.Molecule
import com.iskportal.molekule.core.compounds.atomOf
import com.iskportal.molekule.core.geomertry.Sphere
import com.iskportal.molekule.core.geomertry.isInside
import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.core.lattice.Grid3D
import com.iskportal.molekule.core.utils.toRad
import com.iskportal.molekule.visual.KoolVisualizer
import com.iskportal.molekule.visual.plugins.AxisPlugin
import com.iskportal.molekule.visual.plugins.BoxViewerPlugin

fun main() {

    // a basket for your atoms
    val env = Environment()

    // add to it like any of these methods
    env.add(atomOf(AtomicMass.C, Point(0, 0, 0), 1))
    env.add(H2(Point.randomOrientation * 10))

    // use sequence api to your advantage, or reinvent them yourselves
    Grid3D(20, 20, 20)
        .points
        .map { it * 1.5 } // scale
//        .map { it.rotateZ(45.toRad()) } // rotate
        .map { it + (Point.xHat * 12) } // translate
        .filter { it isInside sphereShape  } // filter
        .map { atomOf(AtomicMass.C, it,1) } // generate
        .forEach { env.add(it) } // add



    // visualize a bit, it helps
    KoolVisualizer()
        .withDefaultConfig()
        .addPlugin(AxisPlugin())
        .addPlugin(BoxViewerPlugin(env.enclosingBox))
        .init()
        .addEnvironment(env)
        .runAndWaitUntilExit()

}

val sphereShape = Sphere(Point(22,12,12), 10)


class H2(origin: Point) : Molecule(type = 3) {
    init {
        val eps = Point.xHat * 0.3
        val h1 = Atom(origin - eps, AtomicMass.H.mass, 4)
        val h2 = Atom(origin + eps, AtomicMass.H.mass, 4)
        atoms.addAll(listOf(h1, h2))

        // bonds.add()
    }
}