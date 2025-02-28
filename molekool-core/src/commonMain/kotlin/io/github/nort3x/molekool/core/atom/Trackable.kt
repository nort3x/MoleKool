package io.github.nort3x.molekool.core.atom

import kotlinx.atomicfu.atomic

abstract class Trackable {

    abstract val type: Int

    val id = idGen.incrementAndGet()


    companion object {
        private val idGen = atomic(0L)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Trackable) return false
        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type
        result = 31 * result + id.hashCode()
        return result
    }
}
