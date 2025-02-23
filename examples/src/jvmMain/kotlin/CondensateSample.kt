import io.github.nort3x.molekool.bind.lammps.AtomStyle
import io.github.nort3x.molekool.bind.lammps.IndexedMolecule
import io.github.nort3x.molekool.bind.lammps.InfoMaps
import io.github.nort3x.molekool.bind.lammps.readInputFileFull
import io.github.nort3x.molekool.bind.lammps.readLampsFullFile
import io.github.nort3x.molekool.bind.lammps.toLammpsInputFile
import io.github.nort3x.molekool.bind.readResource
import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.atom.Atom
import io.github.nort3x.molekool.core.atom.EntityGenerator
import io.github.nort3x.molekool.core.atom.Molecule
import io.github.nort3x.molekool.core.atom.Trackable
import io.github.nort3x.molekool.core.geomertry.Box
import io.github.nort3x.molekool.core.geomertry.isInside
import io.github.nort3x.molekool.core.geomertry.point.length
import io.github.nort3x.molekool.core.lattice.Grid3D
import io.github.nort3x.molekool.core.lattice.crystal.Crystal
import io.github.nort3x.molekool.core.lattice.crystal.usingCrystal
import io.github.nort3x.molekool.core.lattice.spanInAllDirections
import io.github.nort3x.molekool.core.utils.print
import io.github.nort3x.molekool.visual.KoolVisualizer
import io.github.nort3x.molekool.visual.plugins.AxisPlugin
import io.github.nort3x.molekool.visual.plugins.BoxViewerPlugin
import kotlin.math.ceil

fun readCondensateEnv(): Pair<List<IndexedMolecule>, Sequence<List<String>>> {
    val res = readResource("condensate.dat")!!
    return readLampsFullFile(res)
}

fun readInputFile(): Pair<Environment, InfoMaps> {
    val res = readResource("condensate.initial.data")!!
    return readInputFileFull(res)
}

fun main() {

    val seedEnv = run {


        val sampleBox = Box(65 to 85, 60 to 80, 50 to 65)


        val fullEnvInfoMaps = readInputFile()
        val env = Environment()

        val participatingAtomsIndexes = mutableMapOf<Int, Atom>()
        val molecules = mutableListOf<List<Int>>()

        readCondensateEnv()
            .first
            .filterNot { it.subAtoms.any { it.type < 5 } }
            .filter { it.position isInside sampleBox }
            .forEach {
                participatingAtomsIndexes.putAll(it.atomIndex)
                molecules.add(it.atomIndex.keys.toList())
            }


        val participatingAtoms = fullEnvInfoMaps.second.atomMap.filterKeys { participatingAtomsIndexes.contains(it) }
        // reposition
        participatingAtoms.forEach {
            it.value.baseTo(participatingAtomsIndexes[it.key]!!.position)
        }

        val bonds = fullEnvInfoMaps.second.bondMap
            .filterValues { it.subAtoms.all { participatingAtoms.values.contains(it) } }
        val angles = fullEnvInfoMaps.second.angleMap
            .filterValues { it.subAtoms.all { participatingAtoms.values.contains(it) } }
        val dihedrals = fullEnvInfoMaps.second.dihedralMap
            .filterValues { it.subAtoms.all { participatingAtoms.values.contains(it) } }

        val participateMolecule = molecules
            .map { it.map { participatingAtoms[it]!! as Atom }.toMutableList() }
            .map { atoms ->
                Molecule(
                    atoms,
                    bonds.filter { it.value.subAtoms.all { atoms.contains(it) } }.map { it.value }.toMutableList(),
                    angles.filter { it.value.subAtoms.all { atoms.contains(it) } }.map { it.value }.toMutableList(),
                    dihedrals.filter { it.value.subAtoms.all { atoms.contains(it) } }.map { it.value }.toMutableList()
                )
            }

        participateMolecule
            .forEach { env.add(it) }


//        KoolVisualizer()
//            .withDefaultConfig()
//            .addPlugin(BoxViewerPlugin(env.enclosingBox))
//            .addPlugin(BoxViewerPlugin(sampleBox, color = Color.RED))
//            .addPlugin(AxisPlugin())
//            .init()
//            .addEnvironment(env)
//            .runAndWaitUntilExit()

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
    readCondensateEnv().first.forEach {
        env.add(it)
    }


    val simulationBox = env.enclosingBox
    val sheetBox = sheetEnv.enclosingBox(0.2)

    env.clear()
    env.boundingBox = simulationBox

//    val middle = env.enclosingBox.middle
//    val centerOfMass = sheetEnv.atoms.map { it.position }.average()
//    sheetEnv.atoms.forEach {
//        it.move(middle - centerOfMass)
//    }

    val xLen = simulationBox.xBoundary.length / seedEnv.enclosingBox.xBoundary.length;
    val yLen = simulationBox.yBoundary.length / seedEnv.enclosingBox.yBoundary.length;
    val zLen = simulationBox.zBoundary.length / seedEnv.enclosingBox.zBoundary.length;


    Grid3D(ceil(xLen).toInt(), ceil(yLen).toInt(), ceil(zLen).toInt())
        .points
        .spanInAllDirections()
        .usingCrystal(Crystal.fromEnvironmentMolecules(seedEnv))
        .filter { it.position isInside simulationBox }
        .filterNot { it.position isInside sheetBox }
        .shuffled()
        .forEachIndexed() { i, it ->
            env.add(it)
        }

    env.entities.addAll(sheetEnv.entities)


    KoolVisualizer()
        .withDefaultConfig()
        .addPlugin(BoxViewerPlugin(env.enclosingBox))
        .addPlugin(AxisPlugin())
        .init()
        .addEnvironment(env)
        .runAndWaitUntilExit()

    env.toLammpsInputFile("input.lmp", atomStyle = AtomStyle.FULL)

}
