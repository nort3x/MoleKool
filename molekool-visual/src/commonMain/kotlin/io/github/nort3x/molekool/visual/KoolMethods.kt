package io.github.nort3x.molekool.visual

import de.fabmax.kool.KoolConfig
import de.fabmax.kool.KoolContext

expect fun createContext(cfg: KoolConfig): KoolContext
expect fun defaultKoolConfig(): KoolConfig
