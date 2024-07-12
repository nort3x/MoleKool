package com.iskportal.molekule.visual.plugins

import com.iskportal.molekule.core.Environment
import com.iskportal.molekule.visual.KoolVisualizer

interface KoolVizualizerPlugin {
    fun initialize(koolVisualizer: KoolVisualizer)
    fun addEnvironment(koolVisualizer: KoolVisualizer, environment: com.iskportal.molekule.core.Environment)
}