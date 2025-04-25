package io.github.nort3x.molekool.bind

import java.io.OutputStream

fun OutputStream.writeLine(string: String): OutputStream =
    this.also {
        it.write("$string\n".encodeToByteArray())
    }

operator fun OutputStream.plus(string: String) = writeLine(string)

inline fun readResource(name: String) = object {}.javaClass.classLoader.getResourceAsStream(name)?.bufferedReader()?.readText()
