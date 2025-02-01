package io.github.nort3x.molekool.visual.plugins

import de.fabmax.kool.math.randomF
import de.fabmax.kool.math.set
import de.fabmax.kool.modules.ksl.KslPbrShader
import de.fabmax.kool.scene.addColorMesh
import de.fabmax.kool.util.Color
import io.github.nort3x.molekool.core.atom.Atom
import io.github.nort3x.molekool.visual.KoolVisualizer
import io.github.nort3x.molekool.visual.utils.vec3
import kotlin.math.tanh
import kotlin.random.Random

val defaultShader = KslPbrShader {
	color { vertexColor(); }
	metallic(0f)
	roughness(0.25f)
	colorCfg.uniformColor(Color.WHITE)
}

class EnvironmentMainPlugin : KoolVizualizerPlugin {
	private val rnd = Random(123)

	val colors = mutableMapOf<Int, Color>()
	val radiuses = mutableMapOf<Int, Float>()
	private lateinit var simulator: KoolVisualizer


	private fun addAtom(atom: Atom) {
		with(simulator) {
			scene.addColorMesh {
				generate {
					withColor(colorOfAtom(atom)) {
						uvSphere {
							this.radius = radiusOfAtom(atom)
							this.center.set(atom.position.vec3)
							this.steps = 2
						}
					}
				}
				this.shader = io.github.nort3x.molekool.visual.plugins.defaultShader
			}
		}
	}

	private fun colorOfAtom(atom: Atom): Color = colors[atom.type] ?: run {
		colors[atom.type] = Color(rnd.randomF(), rnd.randomF(), rnd.randomF())
		colors[atom.type]!!
	}


	private fun radiusOfAtom(atom: Atom): Float = radiuses[atom.type] ?: run {
		radiuses[atom.type] = 0.2f + tanh(atom.mass / 20).toFloat()
		radiuses[atom.type]!!
	}

	override fun initialize(koolVisualizer: KoolVisualizer) {
		this.simulator = koolVisualizer
	}


	override fun addEnvironment(
		koolVisualizer: KoolVisualizer,
		environment: io.github.nort3x.molekool.core.Environment,
	) {

		(koolVisualizer.scene.camera).position.set(0f, 0f, 20f)

		koolVisualizer.orbitInputTransform.translation.set(
			(environment.enclosingBox.middle).vec3
		)
		koolVisualizer.orbitInputTransform.setZoom(10.0, 0.0, 1000.0)

		environment.atoms.forEach {
			addAtom(it)
		}
	}
}