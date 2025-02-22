import io.github.nort3x.molekool.bind.lammps.InfoMaps
import io.github.nort3x.molekool.bind.lammps.readInputFileFull
import io.github.nort3x.molekool.bind.lammps.readLampsFullFile
import io.github.nort3x.molekool.bind.readResource
import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.geomertry.Box
import io.github.nort3x.molekool.core.geomertry.isInside
import io.github.nort3x.molekool.core.geomertry.point.length
import io.github.nort3x.molekool.core.lattice.Grid3D
import io.github.nort3x.molekool.core.lattice.crystal.Crystal
import io.github.nort3x.molekool.core.lattice.crystal.usingCrystal
import io.github.nort3x.molekool.visual.KoolVisualizer
import io.github.nort3x.molekool.visual.plugins.BoxViewerPlugin

fun readCondensateEnv(): List<Molecule> {
    val res = readResource("condensate.dat")!!
    return readLampsFullFile(res)
        .first
        .filterNot { it.atoms.any { it.type < 5 } }
}

fun readInputFile(): Pair<Environment, InfoMaps> {
    val res = readResource("condensate.initial.data")!!
    return readInputFileFull(res)
}

fun main() {

    val seedEnv = run {


        val sampleBox = Box(65 to 85, 60 to 80, 50 to 65)

        val env = Environment()

        readCondensateEnv()
            .filterNot { it.atoms.any { it.type < 5 } }
            .filter { it.position isInside sampleBox }
            .forEachIndexed { i, it ->

                env.add(it)
            }

        val fullEnvInfoMaps = readInputFile()

        return@run env
    }

    val sheetEnv = run {
        with(readInputFile()) {
            val fullEnv = first

            val sheetAtoms = fullEnv.atoms.filter { it.type <= 4 }.toSet()
            val sheetBonds = fullEnv.bond.filter { it.subAtoms.all { sheetAtoms.contains(it) } }
            val sheetAngles = fullEnv.angles.filter { it.subAtoms.all { sheetAtoms.contains(it) } }
            val sheetDihedrals = fullEnv.dihedral.filter { it.subAtoms.all { sheetAtoms.contains(it) } }
            val sheetMolecules = fullEnv.molecules.filter { it.atoms.all { sheetAtoms.contains(it) } }

            fullEnv.clear()
            fullEnv.add(sheetAtoms.toList())
            fullEnv.add(sheetBonds)
            fullEnv.add(sheetAngles)
            fullEnv.add(sheetDihedrals)
            fullEnv.add(sheetMolecules)

            fullEnv
        }
    }

    val env = Environment()
    readCondensateEnv().forEach {
        env.add(it)
    }

    val simulationBox = env.enclosingBox

    env.clear()

    val xLen = simulationBox.xBoundary.length / seedEnv.enclosingBox.xBoundary.length;
    val yLen = simulationBox.yBoundary.length / seedEnv.enclosingBox.yBoundary.length;
    val zLen = simulationBox.zBoundary.length / seedEnv.enclosingBox.zBoundary.length;

    Grid3D(xLen.toInt(), yLen.toInt(), zLen.toInt())
        .points
        .usingCrystal(Crystal.fromEnvironmentMolecules(seedEnv))
        .shuffled()
        .forEachIndexed { i, it ->
            if (i % 2 == 0)
                env.add(it)
        }


    KoolVisualizer()
        .withDefaultConfig()
        .addPlugin(BoxViewerPlugin(env.enclosingBox))
        .init()
        .addEnvironment(env)
        .runAndWaitUntilExit()


}
