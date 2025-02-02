package io.github.nort3x.molekool.core.format

import io.github.nort3x.molekool.core.geomertry.Triangle
import io.github.nort3x.molekool.core.geomertry.point.Point

class STL {

    /**
     * Parses an STL file, automatically detecting whether it is ASCII or binary.
     *
     * @param byteArray The STL file content as a byte array.
     * @return A list of triangles representing the STL file.
     * @throws IllegalArgumentException If the STL file is malformed.
     */
    fun parseSTLData(byteArray: ByteArray): List<Triangle> {
        val isASCII = isASCIISTL(byteArray)
        return if (isASCII) {
            readASCII(byteArray.decodeToString())
        } else {
            readBinary(byteArray)
        }
    }

    /**
     * Determines if the STL file is in ASCII format.
     *
     * @param byteArray The STL file content as a byte array.
     * @return `true` if the file is ASCII, `false` if it is binary.
     */
    private fun isASCIISTL(byteArray: ByteArray): Boolean {
        // Read the first 512 bytes to check for ASCII keywords
        val header = byteArray.copyOfRange(0, minOf(512, byteArray.size)).decodeToString()
        val lines = header.split('\n')

        // Check if the file starts with "solid" and contains "facet" and "vertex"
        return lines.any { it.trim().startsWith("solid") } &&
            lines.any { it.trim().startsWith("facet") } &&
            lines.any { it.trim().startsWith("vertex") }
    }

    /**
     * Parses a binary STL file.
     *
     * @param byteArray The STL file content as a byte array.
     * @return A list of triangles.
     * @throws IllegalArgumentException If the file is malformed.
     */
    private fun readBinary(byteArray: ByteArray): List<Triangle> {
        require(byteArray.size >= 84) { "Invalid binary STL file: File too small" }

        val input = DataInputStream(byteArray)
        val triangles = mutableListOf<Triangle>()

        // Skip the 80-byte header
        input.skip(80)

        // Read the number of triangles
        val numTriangles = input.readInt()

        // Each triangle is 50 bytes (12 bytes for normal + 36 bytes for vertices + 2 bytes for attributes)
        require(byteArray.size == 84 + numTriangles * 50) { "Invalid binary STL file: Incorrect file size" }

        // Read all triangles
        repeat(numTriangles) {
            // Read normal vector (unused in this implementation)
            input.skip(12)

            // Read vertices
            val vertices = Array(3) {
                Point(
                    input.readFloat().toDouble(),
                    input.readFloat().toDouble(),
                    input.readFloat().toDouble(),
                )
            }

            // Skip attribute byte count
            input.skip(2)

            triangles.add(Triangle(vertices[0], vertices[1], vertices[2]))
        }

        return triangles
    }

    /**
     * Parses an ASCII STL file.
     *
     * @param content The STL file content as a string.
     * @return A list of triangles.
     * @throws IllegalArgumentException If the file is malformed.
     */
    private fun readASCII(content: String): List<Triangle> {
        val triangles = mutableListOf<Triangle>()
        val lines = content.split('\n')
        var i = 0

        while (i < lines.size) {
            val line = lines[i].trim()

            if (line.startsWith("facet")) {
                // Read normal vector (unused in this implementation)
                i++

                // Skip "outer loop"
                i++

                // Read vertices
                val vertices = Array(3) {
                    val vertexLine = lines[i].trim()
                    require(vertexLine.startsWith("vertex")) { "Malformed STL file: Expected vertex" }
                    val coords = vertexLine.split(' ').filter { it.isNotBlank() }.drop(1).map { it.toDouble() }
                    require(coords.size == 3) { "Malformed STL file: Invalid vertex coordinates" }
                    i++
                    Point(coords[0], coords[1], coords[2])
                }

                // Skip "endloop" and "endfacet"
                i += 2

                triangles.add(Triangle(vertices[0], vertices[1], vertices[2]))
            } else {
                i++
            }
        }

        return triangles
    }

    /**
     * Helper class to read binary data from a byte array.
     */
    private class DataInputStream(private val byteArray: ByteArray) {

        /**
         * Converts a 4-byte array to a little-endian integer.
         */
        fun ByteArray.toIntLittleEndian(): Int {
            require(size == 4) { "Byte array must be exactly 4 bytes long" }
            return (this[0].toInt() and 0xff) or
                ((this[1].toInt() and 0xff) shl 8) or
                ((this[2].toInt() and 0xff) shl 16) or
                ((this[3].toInt() and 0xff) shl 24)
        }

        private var position = 0

        fun skip(bytes: Int) {
            position += bytes
        }

        fun readInt(): Int {
            val value = byteArray.copyOfRange(position, position + 4).toIntLittleEndian()
            position += 4
            return value
        }

        fun readFloat(): Float {
            val bits = byteArray.copyOfRange(position, position + 4).toIntLittleEndian()
            position += 4
            return Float.fromBits(bits)
        }
    }
}
