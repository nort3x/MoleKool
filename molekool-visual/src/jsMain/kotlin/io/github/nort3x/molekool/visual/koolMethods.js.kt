package io.github.nort3x.molekool.visual

import de.fabmax.kool.KoolConfig
import de.fabmax.kool.KoolConfigJs
import de.fabmax.kool.KoolContext

actual fun createContext(cfg: KoolConfig): KoolContext =
	de.fabmax.kool.createContext(cfg as KoolConfigJs)

actual fun defaultKoolConfig(): KoolConfig =
	KoolConfigJs()