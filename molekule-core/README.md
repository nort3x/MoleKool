## Molekule-Gen
An objective toolkit to generate initial configurations in Molecular Dynamics  

### Advantages:
* procedural: use full power of a programming language
* open source: configure it as needed - you are not bounded to a couple of plugins
* easy to use: reviewing simple examples is all the documentation you require
* community driven: community power = airtight, battle tested, clean code and extensive
* reusable: don't throw away your configurations - write it smart and reuse it else where

### DisAdvantages:
* requires more time to write your configuration
* requires shallow knowledge of a programming language - in this case kotlin

### how can i use this?

add jitpack to your repositories:
```kotlin
repositories {
    mavenCentral()
    maven("https://jitpack.io")
}
```

add molekule-gen to your classpath

```kotlin
dependencies {
    implementation("com.github.Independent-Society-of-Knowledge:molekule:VERSION")
}
```
to include latest version use `-SNAPSHOT` 

[![](https://jitpack.io/v/Independent-Society-of-Knowledge/molekule.svg)](https://jitpack.io/#Independent-Society-of-Knowledge/molekule)

### Here is a glimpse 

```kotlin
package my.awesome.md

import com.iskportal.molekule.core.AtomicMass
import com.iskportal.molekule.core.Environment
import com.iskportal.molekule.core.atom.*
import com.iskportal.molekule.core.geomertry.point.Point
import com.iskportal.molekule.core.utils.toRad


// represent your configuration using classes as base schema
class Hydrogen(position: Point, charge: Double) : ChargedAtom(position, AtomicMass.H.mass, charge, 1)
class Oxygen(position: Point, charge: Double) : ChargedAtom(position, AtomicMass.O.mass, charge, 2)

class OxygenHydrogenBond(first: Oxygen, second: Hydrogen) : Bond(first, second, 1)
class HydrogenOxygenHydrogen(h1: Hydrogen, o: Oxygen, h2: Hydrogen) : Angle(h1, o, h2, 1)


// complex molecules can further confine complexity using encapsulation
// this class represents a water molecule at given point
class WaterMolecule(center: Point) : Molecule(type = 1) {
    init {

        // hydrogen atoms in water molecule are 0.1 nm apart from oxygen
        // with 106 degree in between them
        val length1 = Point(0.1, 0.0, 0.0)
        val length2 = Point(0.1, 0.0, 0.0).rotateZ(106.toRad())

        val h1 = Hydrogen(center + length1, 0.1)
        val h2 = Hydrogen(center + length2, 0.1)
        val o = Oxygen(center, -0.2)

        val bond1 = OxygenHydrogenBond(o, h1)
        val bond2 = OxygenHydrogenBond(o, h2)

        val angle = HydrogenOxygenHydrogen(h1, o, h2)

        // add everything to the bag of molecule
        this.atoms.addAll(arrayOf(h1, o, h2))
        this.bonds.addAll(arrayOf(bond1, bond2))

        this.angles.add(angle)

    }
}

fun main() {

    val env = Environment()

    // cartesian lattice of 10x10x10 cube
    for (i in (0..<10))
        for (j in (0..<10))
            for (k in (0..<10))
                env.add(WaterMolecule(Point(i, j, k)))


    // using other modules (molekule-bind)
    // dump everything to an input file
    // atom full style is for electric charge consideration
    env.toLammpsInputFile("cube-water.data", AtomStyle.FULL)

    // or visualize (molekule-visual)
    KoolVisualizer()
        .withDefaultConfig()
        .addPlugin(SimulationBoxPlugin())
        .addPlugin(BoxViewerPlugin(gasChamber, Color.RED))
        .addPlugin(AxisPlugin())
        .init()
        .addEnvironment(env)
        .runAndWaitUntilExit()
}
```