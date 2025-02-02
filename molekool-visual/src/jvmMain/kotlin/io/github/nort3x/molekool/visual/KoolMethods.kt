package io.github.nort3x.molekool.visual

import de.fabmax.kool.KoolConfig
import de.fabmax.kool.KoolConfigJvm
import de.fabmax.kool.KoolContext

actual fun createContext(cfg: KoolConfig): KoolContext =
    de.fabmax.kool.createContext(cfg as KoolConfigJvm)

actual fun defaultKoolConfig(): KoolConfig =
    KoolConfigJvm()
