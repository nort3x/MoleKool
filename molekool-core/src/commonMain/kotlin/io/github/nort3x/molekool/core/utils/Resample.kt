package io.github.nort3x.molekool.core.utils

import kotlin.random.Random

/**
 * randomly chooses [fraction] of given population (uniformly)
 */
fun <T> Sequence<T>.resample(fraction: Double = 0.5) = filter { Random.nextDouble() < fraction }
