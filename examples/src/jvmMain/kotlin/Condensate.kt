import io.github.nort3x.molekool.bind.lammps.AtomStyle
import io.github.nort3x.molekool.bind.lammps.toLammpsInputFile
import io.github.nort3x.molekool.bind.readResource
import io.github.nort3x.molekool.core.Environment
import io.github.nort3x.molekool.core.lattice.Grid3D
import io.github.nort3x.molekool.core.lattice.crystal.Crystal
import io.github.nort3x.molekool.core.lattice.crystal.usingCrystal

fun main() {
    val seedEnv = run {
        val res = readResource("condensate.dat")!!

        val env = Environment()
//        readLampsFullFile(res)
//            .first
//            .filterNot { it.atoms.any { it.type < 5 } }
//            .forEachIndexed { i, it ->
//                env.add(it)
//            }

//        KoolVisualizer()
//            .withDefaultConfig()
//            .addPlugin(BoxViewerPlugin(env.enclosingBox))
//            .init()
//            .addEnvironment(env)
//            .runAndWaitUntilExit()

        return@run env
    }

    val env = Environment()

    var total = 0.0

    Grid3D(5, 5, 10)
        .points
        .usingCrystal(Crystal.fromEnvironmentMolecules(seedEnv))
        .apply {
            total = count().toDouble()
        }
        .forEachIndexed { i, it ->
            env.add(it)
            if (i % 10 == 0) {
                println("done %${i * 100 / total}")
            }
        }

    println("done creating environemnt")

    env.toLammpsInputFile("prose.10x10.dat", AtomStyle.FULL)
    println("done writing inputfile")

//    KoolVisualizer()
//        .withDefaultConfig()
//        .addPlugin(BoxViewerPlugin(env.enclosingBox))
//        .init()
//        .addEnvironment(env)
//        .runAndWaitUntilExit()
}
