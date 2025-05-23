package io.github.nort3x.molekool.core.utils

fun <T> T.loopApply(count: Int, action: T.() -> T): T {
    var acc = this
    for (i in 0 until count) {
        acc = acc.action()
    }
    return acc
}
