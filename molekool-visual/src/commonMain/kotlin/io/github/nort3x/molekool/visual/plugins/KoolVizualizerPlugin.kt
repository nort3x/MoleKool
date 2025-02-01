package io.github.nort3x.molekool.visual.plugins

import io.github.nort3x.molekool.visual.KoolVisualizer

interface KoolVizualizerPlugin {
    fun initialize(koolVisualizer: KoolVisualizer)
    fun addEnvironment(koolVisualizer: KoolVisualizer, environment: io.github.nort3x.molekool.core.Environment)
}